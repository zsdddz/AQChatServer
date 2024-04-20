package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
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

        // 事先清理超时的登陆时间

        // 获取系统当前时间
        final long currTime = System.currentTimeMillis();

        AQChatMsgProtocol.UserLoginAck.Builder builder = AQChatMsgProtocol.UserLoginAck.newBuilder();
        AQChatMsgProtocol.UserLoginAck userLoginAck = builder.setUserId(1)
                .setUserName(userName)
                .setUserAvatar(userAvatar)
                .build();
        ctx.writeAndFlush(userLoginAck);
    }
}
