package com.howcode.aqchat.message;

import com.howcode.aqchat.common.enums.AQChatEnum;
import com.howcode.aqchat.common.model.AliOssStsDto;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-24 0:55
 */
public class MessageConstructor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConstructor.class);

    public static AQChatMsgProtocol.ExceptionMsg buildExceptionMsg(AQChatEnum aqChatEnum){
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

    public static AQChatMsgProtocol.RecoverUserAck buildRecoverUserAck(UserGlobalInfoDto userLoginInfo) {
        return AQChatMsgProtocol.RecoverUserAck.newBuilder()
                .setUserId(userLoginInfo.getUserId())
                .setUserName(userLoginInfo.getUserName())
                .setRoomId(userLoginInfo.getRoomId())
                .setUserAvatar(userLoginInfo.getUserAvatar())
                .build();
    }

    public static AQChatMsgProtocol.UserLogoutAck buildUserLogoutAck(String userId) {
        return AQChatMsgProtocol.UserLogoutAck.newBuilder().setUserId(userId).build();
    }
}
