package com.howcode.aqchat.handler.impl;


import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.common.utils.IdProvider;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IUserHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.service.service.IAQUserService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-20 18:58
 */
@Component
public class UserLoginCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.UserLoginCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Resource
    private IUserHolder userHolder;

    @Resource
    private GlobalChannelHolder globalChannelHolder;

    @Resource
    private IAQUserService userService;


    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.UserLoginCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        //判断用户是否已经登录
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).get();
        if (null != userId) {
            LOGGER.info("UserLoginCmdHandler handle, user already login, userId:{}", userId);
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_ALREADY_LOGIN);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        String userName = cmd.getUserName();
        String userAvatar = cmd.getUserAvatar();
        userId = IdProvider.generateUserId();

        //将登录信息保存至redis
        UserGlobalInfoDto userGlobalInfoDto = new UserGlobalInfoDto();
        userGlobalInfoDto.setUserId(userId);
        userGlobalInfoDto.setUserName(userName);
        userGlobalInfoDto.setUserAvatar(userAvatar);
        userHolder.saveUserInfo(userGlobalInfoDto);

        LOGGER.info("UserLoginCmdHandler handle, userId:{}, userName:{}, userAvatar:{}", userId, userName, userAvatar);
        userService.saveUser(userId, userName, userAvatar);

        //添加用户channel
        globalChannelHolder.put(userId, (NioSocketChannel) ctx.channel());
        //添加userId到channel
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).set(userId);
        LOGGER.info("UserLoginCmdHandler handle, user login success, userId:{}", userId);
        AQChatMsgProtocol.UserLoginAck.Builder builder = AQChatMsgProtocol.UserLoginAck.newBuilder();
        AQChatMsgProtocol.UserLoginAck userLoginAck = builder.setUserId(userId)
                .setUserName(userName)
                .setUserAvatar(userAvatar)
                .build();
        ctx.writeAndFlush(userLoginAck);
    }
}
