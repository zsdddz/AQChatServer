package com.howcode.aqchat.handler.impl;


import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.common.utils.IdProvider;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IUserHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.service.service.IAQUserService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-20 18:58
 */
@Component
public class UserLoginCmdHandler implements ICmdHandler<AQChatMsgProtocol.UserLoginCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Resource
    @Lazy
    private IUserHolder userHolder;

    @Resource
    @Lazy
    private GlobalChannelHolder channelHolder;

    @Resource
    @Lazy
    private IAQUserService userService;


    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.UserLoginCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        String userName = cmd.getUserName();
        String userAvatar = cmd.getUserAvatar();
        String userId = IdProvider.generateUserId();
        //添加userId到channel
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).set(userId);

        //将登录信息保存至redis
        UserGlobalInfoDto userGlobalInfoDto = new UserGlobalInfoDto();
        userGlobalInfoDto.setUserId(userId);
        userGlobalInfoDto.setUserName(userName);
        userGlobalInfoDto.setUserAvatar(userAvatar);
        userHolder.saveUserInfo(userGlobalInfoDto);

        LOGGER.info("UserLoginCmdHandler handle, userId:{}, userName:{}, userAvatar:{}", userId, userName, userAvatar);
        userService.saveUser(userId, userName, userAvatar);

        //添加用户channel
        channelHolder.put(userId, (NioSocketChannel) ctx.channel());

        LOGGER.info("UserLoginCmdHandler handle, user login success, userId:{}", userId);
        AQChatMsgProtocol.UserLoginAck.Builder builder = AQChatMsgProtocol.UserLoginAck.newBuilder();
        AQChatMsgProtocol.UserLoginAck userLoginAck = builder.setUserId(userId)
                .setUserName(userName)
                .setUserAvatar(userAvatar)
                .build();
        ctx.writeAndFlush(userLoginAck);
    }
}
