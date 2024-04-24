package com.howcode.aqchat.message;

import com.howcode.aqchat.enums.AQChatEnum;
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
}
