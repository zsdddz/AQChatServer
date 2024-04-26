package com.howcode.aqchat.holder;


import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import com.howcode.aqchat.holder.impl.AQUserHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageBroadcaster;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ZhangWeinan
 * @Description: 全局channel持有者 管理和连接相关的channel和数据
 * @date 2024-04-21 22:56
 */
@Component
public class GlobalChannelHolder {
    private final Map<String, NioSocketChannel> CHANNELS = new ConcurrentHashMap<>();

    @Resource
    private MessageBroadcaster messageBroadcaster;

    @Resource
    private RedisCacheHelper redisCacheHelper;

    @Resource
    private AQUserHolder aqUserHolder;

    public void put(String userId, NioSocketChannel nioSocketChannel) {
        CHANNELS.put(userId, nioSocketChannel);
    }

    public NioSocketChannel getUserChannel(String userId) {
        return CHANNELS.get(userId);
    }

    public NioSocketChannel remove(String userId) {
        return CHANNELS.remove(userId);
    }

    /**
     * 获取用户所在房间
     */
    public String getRoomId(String userId) {
        return messageBroadcaster.getUserRoom(userId);
    }

    /**
     * 退出
     *
     * @param userId
     * @return
     */
    public NioSocketChannel logout(String userId) {
        NioSocketChannel nioSocketChannel = getUserChannel(userId);
        String roomId = messageBroadcaster.leaveRoom(userId, nioSocketChannel);
        dissolveTheRoom4Logout(roomId);
        return remove(userId);
    }

    /**
     * 离线
     */
    public void offline(String userId,NioSocketChannel nioSocketChannel) {
        messageBroadcaster.removeChannel(userId,nioSocketChannel);
    }

    /**
     * 创建房间
     *
     * @param roomId
     * @return
     */
    public void createChannelGroup(String roomId) {
        ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        messageBroadcaster.addChannelGroup(roomId, channelGroup);
    }

    public void joinRoom(String roomId, String userId, Channel channel) {
        messageBroadcaster.joinRoom(roomId, userId, (NioSocketChannel) channel);
        //添加用户所在房间信息
        UserGlobalInfoDto userLoginInfo = aqUserHolder.getUserInfo(userId);
        userLoginInfo.setRoomId(roomId);
        aqUserHolder.saveUserInfo(userLoginInfo);
    }

    public void leaveRoom(String userId, Channel channel) {
        messageBroadcaster.leaveRoom(userId, (NioSocketChannel) channel);
        //删除用户所在房间信息
        UserGlobalInfoDto userLoginInfo = aqUserHolder.getUserInfo(userId);
        userLoginInfo.setRoomId(null);
        aqUserHolder.saveUserInfo(userLoginInfo);
    }

    public void isOrNoDissolveTheRoom(String roomId, Integer roomNo) {
        ChannelGroup channelGroup = messageBroadcaster.getChannelGroup(roomId);
        if (null == channelGroup || channelGroup.isEmpty()) {
            redisCacheHelper.deleteObject(AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo);
            redisCacheHelper.deleteObject(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId);
        }
        //解散房间
        messageBroadcaster.removeChannelGroup(roomId);
    }

    public void dissolveTheRoom4Logout(String roomId) {
        //获取房间信息
        RoomInfoDto roomInfoDto = getRoomInfo(roomId);
        if (null == roomInfoDto) {
            return;
        }
        isOrNoDissolveTheRoom(roomId, roomInfoDto.getRoomNo());
    }

    /**
     * 获取房间信息
     */
    public RoomInfoDto getRoomInfo(String roomId) {
        return redisCacheHelper.getCacheObject(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, RoomInfoDto.class);
    }

    public void sendBroadcastMessage(MessageDto messageDto) {
        if (null == messageDto) {
            return;
        }
        UserGlobalInfoDto userInfo = aqUserHolder.getUserInfo(messageDto.getSenderId());
        if (null == userInfo) {
            return;
        }
        AQChatMsgProtocol.User.Builder userBuilder = AQChatMsgProtocol.User.newBuilder();
        userBuilder.setUserId(userInfo.getUserId());
        userBuilder.setUserName(userInfo.getUserName());
        userBuilder.setUserAvatar(userInfo.getUserAvatar());
        AQChatMsgProtocol.BroadcastMsgAck broadcastMsgAck = AQChatMsgProtocol.BroadcastMsgAck.newBuilder()
                .setRoomId(messageDto.getRoomId())
                .setUser(userBuilder.build())
                .setMsgType(AQChatMsgProtocol.MsgType.forNumber(messageDto.getMessageType()))
                .setMsg(messageDto.getMessageContent())
                .build();
        messageBroadcaster.broadcast(userInfo.getRoomId(), broadcastMsgAck);
    }
}
