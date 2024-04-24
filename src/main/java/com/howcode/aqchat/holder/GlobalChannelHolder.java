package com.howcode.aqchat.holder;

import com.howcode.aqchat.constant.AQChatConstant;
import com.howcode.aqchat.message.MessageBroadcaster;
import com.howcode.aqchat.model.RoomInfoDto;
import com.howcode.aqchat.utils.RedisCacheHelper;
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
 * @Description:
 * @date 2024-04-21 22:56
 */
@Component
public class GlobalChannelHolder {
    private final Map<String, NioSocketChannel> CHANNELS = new ConcurrentHashMap<>();

    @Resource
    private MessageBroadcaster messageBroadcaster;

    @Resource
    private RedisCacheHelper redisCacheHelper;

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
        messageBroadcaster.leaveRoom(userId, nioSocketChannel);
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

    public void dissolveTheRoom4Logout(String userId) {
        String roomId = getRoomId(userId);
        if (null == roomId) {
            return;
        }
        //获取房间信息
        RoomInfoDto roomInfoDto = redisCacheHelper.getCacheObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, RoomInfoDto.class);
        if (null == roomInfoDto) {
            return;
        }
        isOrNoDissolveTheRoom(roomId, roomInfoDto.getRoomNo());
    }
}
