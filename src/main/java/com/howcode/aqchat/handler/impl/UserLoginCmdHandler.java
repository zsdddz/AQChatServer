package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.constant.AQChatConstant;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.utils.IdProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-20 18:58
 */
@Component
public class UserLoginCmdHandler implements ICmdHandler<AQChatMsgProtocol.UserLoginCmd> {


    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.UserLoginCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        String userName = cmd.getUserName();
        String userAvatar = cmd.getUserAvatar();
        String userId = IdProvider.generateUserId();

        //添加userId到channel
        ctx.channel().attr(AttributeKey.valueOf(AQChatConstant.AQBusinessConstant.USER_ID)).set(userId);

        AQChatMsgProtocol.UserLoginAck.Builder builder = AQChatMsgProtocol.UserLoginAck.newBuilder();
        AQChatMsgProtocol.UserLoginAck userLoginAck = builder.setUserId(userId)
                .setUserName(userName)
                .setUserAvatar(userAvatar)
                .build();
        ctx.writeAndFlush(userLoginAck);
    }
}
