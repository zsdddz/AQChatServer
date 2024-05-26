package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.mq.MqSendingAgent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 22:28
 */
@Component
public class UserLogoutCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.UserLogoutCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogoutCmdHandler.class);
    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    @Lazy
    private MqSendingAgent mqSendingAgent;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.UserLogoutCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        //获取用户id
        String userId = verifyLogin(ctx);
        if (userId == null) {
            ctx.close();
            return;
        }
        //退出
        if (!userId.equals(cmd.getUserId())) {
            //用户id不匹配
            LOGGER.error("[非法操作]用户id不匹配，强制断开客户端连接");
            ctx.writeAndFlush(MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_ID_NOT_MATCH));
            return;
        }
        //清除登录属性
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).set(null);
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).set(null);
        //mq发送用户退出房间消息
        mqSendingAgent.sendLeaveRoomMsg(userId,globalChannelHolder.getRoomId(userId));
        //mq发送用户退出消息
        mqSendingAgent.sendLogoutMessage(userId);
        //退出
        globalChannelHolder.logout(userId);
        LOGGER.info("用户{}退出", userId);
        ctx.writeAndFlush(MessageConstructor.buildUserLogoutAck(userId));
    }
}
