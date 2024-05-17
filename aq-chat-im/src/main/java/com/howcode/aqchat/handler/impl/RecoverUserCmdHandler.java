package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.impl.AQUserHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
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
 * @date 2024-04-26 21:03
 */
@Component
public class RecoverUserCmdHandler implements ICmdHandler<AQChatMsgProtocol.RecoverUserCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverUserCmdHandler.class);
    @Resource
    @Lazy
    private AQUserHolder aqUserHolder;
    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.RecoverUserCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        String userId = cmd.getUserId();
        UserGlobalInfoDto userInfo = aqUserHolder.getUserInfo(userId);
        if (null == userInfo) {
            //离线时间过长
            ctx.writeAndFlush(MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_DOES_NOT_EXIST_OR_EXITS));
            return;
        }
        //重新登录 续期
        //添加用户channel
        globalChannelHolder.put(userId, (NioSocketChannel) ctx.channel());
        aqUserHolder.saveUserInfo(userInfo);
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).set(userId);
        //判断是否有房间
        if (null != userInfo.getRoomId()) {
            //加入房间
            globalChannelHolder.joinRoom(userInfo.getRoomId(),userId,ctx.channel());
        }
        AQChatMsgProtocol.RecoverUserAck recoverUserAck = MessageConstructor.buildRecoverUserAck(userInfo);
        ctx.writeAndFlush(recoverUserAck);
    }
}
