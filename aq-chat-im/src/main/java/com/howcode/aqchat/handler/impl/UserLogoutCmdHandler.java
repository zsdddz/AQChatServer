package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.handler.ICmdHandler;
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
public class UserLogoutCmdHandler implements ICmdHandler<AQChatMsgProtocol.UserLogoutCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogoutCmdHandler.class);
    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    @Lazy
    private MqSendingAgent mqSendingAgent;
    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.UserLogoutCmd cmd) {
        if (null == ctx|| null == cmd) {
            return;
        }
        //获取用户id
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).get();
        if (userId == null){
            ctx.writeAndFlush(MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_NOT_LOGIN));
            ctx.close();
            return;
        }
        //退出
        if (!userId.equals(cmd.getUserId())){
            //用户id不匹配
            ctx.writeAndFlush(MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_ID_NOT_MATCH));
            return;
        }
        //mq发送用户退出消息
        mqSendingAgent.sendLogoutMessage(userId);
        //退出
        globalChannelHolder.logout(userId);
        LOGGER.info("用户{}退出",userId);
        ctx.writeAndFlush(MessageConstructor.buildUserLogoutAck(userId));
        ctx.close();
    }
}