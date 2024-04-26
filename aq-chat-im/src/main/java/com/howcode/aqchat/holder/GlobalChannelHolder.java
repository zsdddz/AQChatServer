package com.howcode.aqchat.holder;


import com.howcode.aqchat.common.constant.AQChatConstant;
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
    }

    public void leaveRoom(String userId, Channel channel) {
        messageBroadcaster.leaveRoom(userId, (NioSocketChannel) channel);
    }

    public void isOrNoDissolveTheRoom(String roomId, Integer roomNo) {
        ChannelGroup channelGroup = messageBroadcaster.getChannelGroup(roomId);
        if (null == channelGroup || channelGroup.isEmpty()) {
            redisCacheHelper.deleteObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo);
            redisCacheHelper.deleteObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId);
        }
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
        return redisCacheHelper.getCacheObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, RoomInfoDto.class);
    }

    public void sendMsgToRoom(String userId, AQChatMsgProtocol.SendMsgCmd cmd) {
        UserGlobalInfoDto userLoginInfo = aqUserHolder.getUserLoginInfo(userId);
        if (null == userLoginInfo) {
            return;
        }
        messageBroadcaster.sendMsgToRoom(userLoginInfo, cmd);
    }
}
