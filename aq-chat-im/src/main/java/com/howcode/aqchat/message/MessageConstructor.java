package com.howcode.aqchat.message;

import com.howcode.aqchat.common.enums.AQChatEnum;
import com.howcode.aqchat.common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-24 0:55
 */
public class MessageConstructor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConstructor.class);

    public static AQChatMsgProtocol.ExceptionMsg buildExceptionMsg(AQChatEnum aqChatEnum) {
        LOGGER.error("构建异常消息: code = {}, msg = {}", aqChatEnum.getCode(), aqChatEnum.getMessage());
        return AQChatMsgProtocol.ExceptionMsg.newBuilder().setCode(aqChatEnum.getCode()).setMsg(aqChatEnum.getMessage()).build();
    }

    public static AQChatMsgProtocol.GetStsAck buildGetStsAck(AliOssStsDto aliOssSts) {
        return AQChatMsgProtocol.GetStsAck.newBuilder()
                .setAccessKeyId(aliOssSts.getAccessKeyId())
                .setAccessKeySecret(aliOssSts.getAccessKeySecret())
                .setSecurityToken(aliOssSts.getSecurityToken())
                .setBucket(aliOssSts.getBucket())
                .setRegion(aliOssSts.getRegion())
                .setUploadPath(aliOssSts.getUploadPath())
                .setEndpoint(aliOssSts.getOssEndpoint())
                .build();
    }

    public static AQChatMsgProtocol.RecoverUserAck buildRecoverUserAck(UserGlobalInfoDto userGlobalInfoDto, RoomInfoDto roomInfo) {
        AQChatMsgProtocol.RecoverUserAck.Builder builder = AQChatMsgProtocol.RecoverUserAck.newBuilder();
        AQChatMsgProtocol.Room.Builder room = AQChatMsgProtocol.Room.newBuilder();
        if (userGlobalInfoDto.getUserId() == null) {
            return null;
        }
        builder.setUserId(userGlobalInfoDto.getUserId());
        builder.setUserName(userGlobalInfoDto.getUserName());
        if (null != roomInfo) {
            List<AQChatMsgProtocol.User> users = buildUsers(roomInfo);
            users.forEach(room::addMembers);
            room.setRoomId(roomInfo.getRoomId())
                    .setRoomNo(roomInfo.getRoomNo())
                    .setRoomName(roomInfo.getRoomName())
                    .setAi(roomInfo.getAi());
            builder.setRoom(room);
        }
        builder.setUserAvatar(userGlobalInfoDto.getUserAvatar());
        return builder.build();
    }

    public static AQChatMsgProtocol.UserLogoutAck buildUserLogoutAck(String userId) {
        return AQChatMsgProtocol.UserLogoutAck.newBuilder().setUserId(userId).build();
    }

    public static AQChatMsgProtocol.SyncChatRecordAck buildSyncChatRecordAck(List<MessageRecordDto> messageList) {
        AQChatMsgProtocol.SyncChatRecordAck.Builder builder = AQChatMsgProtocol.SyncChatRecordAck.newBuilder();
        messageList.forEach(message -> {
            AQChatMsgProtocol.User user = AQChatMsgProtocol.User.newBuilder()
                    .setUserId(message.getSenderId())
                    .setUserName(message.getSenderName())
                    .setUserAvatar(message.getSenderAvatar())
                    .build();
            builder.addChatRecords(AQChatMsgProtocol.ChatRecord.newBuilder()
                    .setMsgId(message.getMessageId())
                    .setUser(user)
                    .setMsgType(AQChatMsgProtocol.MsgType.forNumber(message.getMessageType()))
                    .setMessage(message.getMessageContent())
                    .setExt(message.getMessageExt())
                    .setCreateTime(message.getCreateTime())
                    .build());
        });
        return builder.build();
    }

    public static AQChatMsgProtocol.SyncRoomMembersAck buildSyncRoomMembersAck(RoomInfoDto roomInfoDto) {
        AQChatMsgProtocol.SyncRoomMembersAck.Builder builder = AQChatMsgProtocol.SyncRoomMembersAck.newBuilder();
        if (null != roomInfoDto) {
            List<AQChatMsgProtocol.User> users = buildUsers(roomInfoDto);
            users.forEach(builder::addMembers);
            builder.setRoomId(roomInfoDto.getRoomId());
        }
        return builder.build();
    }

    private static List<AQChatMsgProtocol.User> buildUsers(RoomInfoDto roomInfoDto) {
        List<UserGlobalInfoDto> roomMembers = roomInfoDto.getRoomMembers();
        List<AQChatMsgProtocol.User> userList = new ArrayList<>();
        if (null != roomMembers && !roomMembers.isEmpty()) {
            roomMembers.forEach(user -> {
                AQChatMsgProtocol.User roomUser = AQChatMsgProtocol.User.newBuilder()
                        .setUserId(user.getUserId())
                        .setUserName(user.getUserName())
                        .setUserAvatar(user.getUserAvatar())
                        .build();
                userList.add(roomUser);
            });
        }
        return userList;
    }

    public static AQChatMsgProtocol.SendMsgAck buildSendMsgAck(String roomId, String userId, String msgId, String ext) {
        return AQChatMsgProtocol.SendMsgAck.newBuilder()
                .setRoomId(roomId)
                .setUserId(userId)
                .setStatus(true)
                .setMsgId(msgId)
                .setExt(ext)
                .build();
    }

    public static AQChatMsgProtocol.RecallMsgAck buildRecallMsgAck(String msgId, String roomId, String userId) {
        return AQChatMsgProtocol.RecallMsgAck.newBuilder()
                .setMsgId(msgId)
                .setRoomId(roomId)
                .setUserId(userId)
                .setStatus(true)
                .build();
    }

    public static AQChatMsgProtocol.RecallMsgNotify buildRecallMsgNotify(RecallMessageDto recallMessageDto) {
        return AQChatMsgProtocol.RecallMsgNotify.newBuilder()
                .setUserId(recallMessageDto.getUserId())
                .setRoomId(recallMessageDto.getRoomId())
                .setMsgId(recallMessageDto.getMsgId())
                .build();
    }
}
