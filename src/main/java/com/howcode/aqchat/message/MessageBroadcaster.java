package com.howcode.aqchat.message;

import com.google.protobuf.GeneratedMessageV3;
import com.howcode.aqchat.model.UserLoginInfoDto;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ZhangWeinan
 * @Description: 消息广播器
 * @date 2024-04-21 22:16
 */
@Component
public class MessageBroadcaster {

    /**
     * 用户对应的房间
     */
    private final Map<String, String> userRoomMap = new ConcurrentHashMap<>();

    /**
     * 房间号 -> ChannelGroup
     * 管理房间内的所有channel
     */
    private final Map<String, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();

    public void addChannelGroup(String roomId, ChannelGroup channelGroup) {
        channelGroupMap.put(roomId, channelGroup);
    }
    public ChannelGroup getChannelGroup(String roomId) {
        return channelGroupMap.get(roomId);
    }
    public ChannelGroup removeChannelGroup(String roomId) {
        return channelGroupMap.remove(roomId);
    }

    /**
     * 获取用户所在房间
     */
    public String getUserRoom(String userId) {
        return userRoomMap.get(userId);
    }

    /**
     * 加入房间
     */
    public void joinRoom(String roomId, String userId, NioSocketChannel channel) {
        if (null == roomId || null == userId || null == channel) {
            return;
        }
        //判断用户是否已经在房间内
        if (null != userRoomMap.get(userId)) {
            return;
        }
        userRoomMap.put(userId, roomId);
        ChannelGroup channelGroup = channelGroupMap.get(roomId);
        if (null == channelGroup) {
            return;
        }
        channelGroup.add(channel);
    }
    /**
     * 离开房间
     */
    public String leaveRoom(String userId,NioSocketChannel channel) {
        if (null == userId || null == channel) {
            return null;
        }
        String roomId = userRoomMap.remove(userId);
        if (null == roomId) {
            return null;
        }
        ChannelGroup channelGroup = channelGroupMap.get(roomId);
        if (null == channelGroup) {
            return null;
        }
        channelGroup.remove(channel);
        return roomId;
    }

    /**
     * 广播消息
     * @param roomId
     * @param msg
     */
    public <T extends GeneratedMessageV3> void broadcast(String roomId, T msg) {
        ChannelGroup channelGroup = channelGroupMap.get(roomId);
        if (null == channelGroup) {
            return;
        }
        channelGroup.writeAndFlush(msg);
    }

    public void sendMsgToRoom(UserLoginInfoDto userInfo, AQChatMsgProtocol.SendMsgCmd cmd) {
        String roomId = userRoomMap.get(userInfo.getUserId());
        if (null == roomId) {
            return;
        }
        AQChatMsgProtocol.User.Builder userBuilder = AQChatMsgProtocol.User.newBuilder();
        userBuilder.setUserId(userInfo.getUserId());
        userBuilder.setUserName(userInfo.getUserName());
        userBuilder.setUserAvatar(userInfo.getUserAvatar());
        AQChatMsgProtocol.BroadcastMsgAck sendMsgCmd = AQChatMsgProtocol.BroadcastMsgAck.newBuilder()
                .setRoomId(roomId)
                .setUser(userBuilder.build())
                .setMsgType(cmd.getMsgType())
                .setMsg(cmd.getMsg())
                .build();
        broadcast(roomId, sendMsgCmd);
    }
}
