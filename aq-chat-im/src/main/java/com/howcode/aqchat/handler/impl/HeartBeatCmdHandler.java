package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-21 23:19
 */
@Component
public class HeartBeatCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.HeartBeatCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.HeartBeatCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
       //获取userId
        String userId = verifyLogin(ctx);
        if (null == userId) {
            //未登录
            LOGGER.error("HeartBeatCmdHandler handle error, user not login");
            return;
        }
        LOGGER.info("用户{}发送心跳", userId);
        //更新心跳时间
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.HEART_BEAT_TIME)).set(System.currentTimeMillis());
        //返回心跳响应
        ctx.writeAndFlush(AQChatMsgProtocol.HeartBeatAck.newBuilder().setPong(AQBusinessConstant.HEART_BEAT_ACK).build());
    }
}
