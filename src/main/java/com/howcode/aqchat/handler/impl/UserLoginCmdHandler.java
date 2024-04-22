package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.constant.AQChatConstant;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IUserHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.model.UserLoginInfoDto;
import com.howcode.aqchat.utils.IdProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-20 18:58
 */
@Component
public class UserLoginCmdHandler implements ICmdHandler<AQChatMsgProtocol.UserLoginCmd> {

    @Resource
    @Lazy
    private IUserHolder userHolder;

    @Resource
    @Lazy
    private GlobalChannelHolder channelHolder;


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

        //将登录信息保存至redis
        UserLoginInfoDto userLoginInfoDto = new UserLoginInfoDto();
        userLoginInfoDto.setUserId(userId);
        userLoginInfoDto.setUserName(userName);
        userLoginInfoDto.setUserAvatar(userAvatar);
        userHolder.saveUserLoginInfo(userLoginInfoDto);

        //添加用户channel
        channelHolder.put(userId, (NioSocketChannel) ctx.channel());

        AQChatMsgProtocol.UserLoginAck.Builder builder = AQChatMsgProtocol.UserLoginAck.newBuilder();
        AQChatMsgProtocol.UserLoginAck userLoginAck = builder.setUserId(userId)
                .setUserName(userName)
                .setUserAvatar(userAvatar)
                .build();
        ctx.writeAndFlush(userLoginAck);
    }
}
