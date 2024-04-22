package com.howcode.aqchat.holder;

import com.howcode.aqchat.message.MessageBroadcaster;
import io.netty.channel.socket.nio.NioSocketChannel;
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
     * @param userId
     * @return
     */
    public NioSocketChannel logout(String userId){
        NioSocketChannel nioSocketChannel = getUserChannel(userId);
        messageBroadcaster.leaveRoom(userId,nioSocketChannel);
        return remove(userId);
    }
}
