package com.howcode.aqchat.message;

import com.howcode.aqchat.enums.AQChatEnum;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-24 0:55
 */
public class MessageConstructor {

    public static AQChatMsgProtocol.ExceptionMsg buildExceptionMsg(AQChatEnum aqChatEnum){
        return AQChatMsgProtocol.ExceptionMsg.newBuilder().setCode(aqChatEnum.getCode()).setMsg(aqChatEnum.getMessage()).build();
    }
}
