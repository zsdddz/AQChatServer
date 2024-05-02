package com.howcode.aqchat.message;

import com.howcode.aqchat.common.enums.AQChatEnum;
import com.howcode.aqchat.common.model.AliOssStsDto;
import com.howcode.aqchat.common.model.MessageRecordDto;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static AQChatMsgProtocol.RecoverUserAck buildRecoverUserAck(UserGlobalInfoDto userGlobalInfoDto) {
        AQChatMsgProtocol.RecoverUserAck.Builder builder = AQChatMsgProtocol.RecoverUserAck.newBuilder();
        if (userGlobalInfoDto.getUserId() == null) {
            return null;
        }
        builder.setUserId(userGlobalInfoDto.getUserId());
        builder.setUserName(userGlobalInfoDto.getUserName());
        if (null != userGlobalInfoDto.getRoomId()) {
            builder.setRoomId(userGlobalInfoDto.getRoomId());
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
                    .setUser(user)
                    .setMsgType(AQChatMsgProtocol.MsgType.forNumber(message.getMessageType()))
                    .setMessage(message.getMessageContent())
                    .setCreateTime(message.getCreateTime())
                    .build());
        });
        return builder.build();
    }
}
