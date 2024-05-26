package com.howcode.aqchat.holder;

import com.howcode.aqchat.common.model.*;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageBroadcaster;
import com.howcode.aqchat.message.MessageConstructor;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalChannelHolder.class);

    private final Map<String, NioSocketChannel> CHANNELS = new ConcurrentHashMap<>();

    @Resource
    private MessageBroadcaster messageBroadcaster;

    @Resource
    private IUserHolder userHolder;
    
    @Resource
    private IRoomHolder roomHolder;

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
     */
    public void logout(String userId) {
        NioSocketChannel nioSocketChannel = getUserChannel(userId);
        String roomId = messageBroadcaster.leaveRoom(userId, nioSocketChannel);
        dissolveTheRoomByLogout(roomId);
        remove(userId);
    }

    /**
     * 离线
     */
    public void offline(String userId, NioSocketChannel nioSocketChannel) {
        remove(userId);
        messageBroadcaster.removeChannel(userId, nioSocketChannel);
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
        UserGlobalInfoDto userInfo = userHolder.getUserInfo(userId);
        userInfo.setRoomId(roomId);
        userHolder.saveUserInfo(userInfo);
    }

    public void leaveRoom(String userId, Channel channel) {
        messageBroadcaster.leaveRoom(userId, (NioSocketChannel) channel);
        //删除用户所在房间信息
        UserGlobalInfoDto userInfo = userHolder.getUserInfo(userId);
        userInfo.setRoomId(null);
        userInfo.setJoinRoomTime(null);
        userHolder.saveUserInfo(userInfo);
    }

    public void isOrNoDissolveTheRoom(String roomId, Integer roomNo) {
        ChannelGroup channelGroup = messageBroadcaster.getChannelGroup(roomId);
        if (!messageBroadcaster.isTheRoomEmpty(roomId) && (null == channelGroup || channelGroup.isEmpty())) {
            roomHolder.removeRoomInfo(roomNo);
            //解散房间
            messageBroadcaster.removeChannelGroup(roomId);
        }
    }

    public void dissolveTheRoomByLogout(String roomId) {
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
        return roomHolder.getRoomInfoById(roomId);
    }

    public RoomInfoDto getRoomAllInfo(String roomId) {
        return roomHolder.getRoomAllInfoById(roomId);
    }

    public void sendBroadcastMessage(MessageDto messageDto) {
        if (null == messageDto) {
            return;
        }
        UserGlobalInfoDto userInfo = userHolder.getUserInfo(messageDto.getSenderId());
        AQChatMsgProtocol.User.Builder userBuilder = getUserBuilder(userInfo);
        if (userBuilder == null) return;
        AQChatMsgProtocol.BroadcastMsgAck broadcastMsgAck = AQChatMsgProtocol.BroadcastMsgAck.newBuilder()
                .setRoomId(messageDto.getRoomId())
                .setUser(userBuilder.build())
                .setMsgId(messageDto.getMessageId().toString())
                .setMsgType(AQChatMsgProtocol.MsgType.forNumber(messageDto.getMessageType()))
                .setMsg(messageDto.getMessageContent())
                .setExt(messageDto.getMessageExt())
                .build();
        messageBroadcaster.broadcast(userInfo.getRoomId(), broadcastMsgAck);
    }

    public void notifyLogout(String userId) {
        if (null == userId) {
            return;
        }
        UserGlobalInfoDto userInfo = userHolder.getUserInfo(userId);
        if (null == userInfo || null == userInfo.getRoomId() ){
            LOGGER.info("[退出通知] 用户信息或者房间信息为空");
            return;
        }
        AQChatMsgProtocol.User.Builder userBuilder = getUserBuilder(userInfo);
        if (userBuilder == null) return;
        AQChatMsgProtocol.LeaveRoomNotify leaveRoomNotify = AQChatMsgProtocol.LeaveRoomNotify.newBuilder()
                .setUser(userBuilder)
                .setRoomId(userInfo.getRoomId())
                .build();
        messageBroadcaster.broadcast(userInfo.getRoomId(), leaveRoomNotify);
        userHolder.removeUserInfo(userId);
    }

    public void notifyJoinRoom(RoomNotifyDto roomNotifyDto) {
        if (null == roomNotifyDto) {
            return;
        }
        UserGlobalInfoDto userInfo = userHolder.getUserInfo(roomNotifyDto.getUserId());
        AQChatMsgProtocol.User.Builder userBuilder = getUserBuilder(userInfo);
        if (userBuilder == null) return;
        AQChatMsgProtocol.JoinRoomNotify joinRoomNotify = AQChatMsgProtocol.JoinRoomNotify.newBuilder()
                .setUser(userBuilder)
                .setRoomId(roomNotifyDto.getRoomId())
                .build();
        messageBroadcaster.broadcast(userInfo.getRoomId(), joinRoomNotify);
    }

    private static AQChatMsgProtocol.User.Builder getUserBuilder(UserGlobalInfoDto userInfo) {
        if (null == userInfo) {
            return null;
        }
        AQChatMsgProtocol.User.Builder userBuilder = AQChatMsgProtocol.User.newBuilder();
        userBuilder.setUserId(userInfo.getUserId());
        userBuilder.setUserName(userInfo.getUserName());
        userBuilder.setUserAvatar(userInfo.getUserAvatar());
        return userBuilder;
    }

    public void notifyLeaveRoom(RoomNotifyDto roomNotifyDto) {
        if (null == roomNotifyDto) {
            return;
        }
        UserGlobalInfoDto userInfo = userHolder.getUserInfo(roomNotifyDto.getUserId());
        AQChatMsgProtocol.User.Builder userBuilder = getUserBuilder(userInfo);
        if (userBuilder == null) return;
        AQChatMsgProtocol.LeaveRoomNotify leaveRoomNotify = AQChatMsgProtocol.LeaveRoomNotify.newBuilder()
                .setUser(userBuilder)
                .setRoomId(roomNotifyDto.getRoomId())
                .build();
        messageBroadcaster.broadcast(roomNotifyDto.getRoomId(), leaveRoomNotify);
    }

    public void notifyOfflineMessage(String userId) {
        if (null == userId) {
            return;
        }
        UserGlobalInfoDto userInfo = userHolder.getUserInfo(userId);
        if (null == userInfo || null == userInfo.getRoomId() ){
            LOGGER.info("[离线消息] 用户信息或者房间信息为空");
            return;
        }
        AQChatMsgProtocol.User.Builder userBuilder = getUserBuilder(userInfo);
        if (userBuilder == null) return;
        AQChatMsgProtocol.OfflineNotify offlineNotify = AQChatMsgProtocol.OfflineNotify.newBuilder()
                .setUser(userBuilder)
                .setRoomId(userInfo.getRoomId())
                .build();
        messageBroadcaster.broadcast(userInfo.getRoomId(), offlineNotify);
    }

    public void removeByBroadcaster(String userId) {
        messageBroadcaster.remove(userId);
    }

    public void notifyRecallMessage(RecallMessageDto recallMessageDto) {
        if (null == recallMessageDto) {
            return;
        }
        AQChatMsgProtocol.RecallMsgNotify recallMsgNotify = MessageConstructor.buildRecallMsgNotify(recallMessageDto);
        messageBroadcaster.broadcast(recallMessageDto.getRoomId(), recallMsgNotify);
    }
}
