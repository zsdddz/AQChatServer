package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.constant.AQChatConstant;
import com.howcode.aqchat.enums.ExceptionEnum;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.BaseMessageProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-21 23:19
 */
@Component
public class HeartBeatCmdHandler implements ICmdHandler<AQChatMsgProtocol.HeartBeatCmd> {


    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.HeartBeatCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        //更新心跳时间
        ctx.channel().attr(AttributeKey.valueOf(AQChatConstant.AQBusinessConstant.HEART_BEAT_TIME)).set(System.currentTimeMillis());
        //获取userId
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQChatConstant.AQBusinessConstant.USER_ID)).get();
        if (null == userId) {
            //未登录  构建异常消息
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = AQChatMsgProtocol.ExceptionMsg.newBuilder()
                    .setCode(ExceptionEnum.USER_NOT_LOGIN.getCode())
                    .setMsg(ExceptionEnum.USER_NOT_LOGIN.getMessage()).build();
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        //返回心跳响应
        ctx.writeAndFlush(AQChatMsgProtocol.HeartBeatAck.newBuilder().setUserId(userId).build());
    }
}
