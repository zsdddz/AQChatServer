package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.utils.IdProvider;
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

import java.util.Date;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-24 14:08
 */
@Component
public class SendMsgCmdHandler implements ICmdHandler<AQChatMsgProtocol.SendMsgCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMsgCmdHandler.class);
    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    @Lazy
    private MqSendingAgent mqSendingAgent;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.SendMsgCmd cmd) {
        if (ctx == null || cmd == null) {
            return;
        }
        // 获取用户Id
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).get();
        if (null == userId) {
            // 用户未登录
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_NOT_LOGIN);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        // 判断用户是否在房间中
        String roomId = globalChannelHolder.getRoomId(userId);
        if (null == roomId || !roomId.equals(cmd.getRoomId())) {
            // 用户未加入房间
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_NOT_IN_ROOM);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }


        roomId = cmd.getRoomId();
        //判断房间是否存在
        if (null == globalChannelHolder.getRoomId(userId) || null == globalChannelHolder.getRoomInfo(roomId)) {
            // 房间不存在
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.ROOM_NOT_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        // 发送消息
        Long messageId = IdProvider.nextId();
        MessageDto messageDto = new MessageDto();
        messageDto.setMessageId(messageId);
        messageDto.setRoomId(roomId);
        messageDto.setSenderId(userId);
        messageDto.setMessageType(cmd.getMsgType().getNumber());
        messageDto.setMessageContent(cmd.getMsg());
        messageDto.setCreateTime(new Date());
        mqSendingAgent.sendMessageToRoom(messageDto);
        mqSendingAgent.storeMessages(messageDto);

        //返回消息发送成功
        AQChatMsgProtocol.SendMsgAck result = AQChatMsgProtocol.SendMsgAck.newBuilder()
                .setRoomId(roomId)
                .setUserId(userId)
                .setSuccess(true)
                .setMsgId(messageId.toString())
                .build();
        ctx.writeAndFlush(result);
    }
}
