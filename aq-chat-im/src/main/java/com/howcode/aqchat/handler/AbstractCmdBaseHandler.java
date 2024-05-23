package com.howcode.aqchat.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-23 12:07
 */
public abstract class AbstractCmdBaseHandler<T extends GeneratedMessageV3> implements ICmdHandler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCmdBaseHandler.class);

    @Override
    public String verifyLogin(ChannelHandlerContext ctx) {
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).get();
        if (null == userId) {
            // 用户未登录
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_NOT_LOGIN);
            ctx.writeAndFlush(exceptionMsg);
            return null;
        }
        return userId;
    }

    @Override
    public String verifyJoinRoom(ChannelHandlerContext ctx) {
        String roomId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).get();
        if (null == roomId) {
            //用户不在房间中
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_NOT_IN_ROOM);
            ctx.writeAndFlush(exceptionMsg);
            return null;
        }
        return roomId;
    }

}
