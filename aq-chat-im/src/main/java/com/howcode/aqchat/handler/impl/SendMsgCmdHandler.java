package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.utils.IdProvider;
import com.howcode.aqchat.common.utils.SafeUtil;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IMessageHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.mq.MqSendingAgent;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-24 14:08
 */
@Component
public class SendMsgCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.SendMsgCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMsgCmdHandler.class);
    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    @Lazy
    private MqSendingAgent mqSendingAgent;
    @Resource
    @Lazy
    private IMessageHolder messageHolder;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.SendMsgCmd cmd) {
        if (ctx == null || cmd == null) {
            return;
        }
        String msgId = cmd.getMsgId();
        // 判断消息Id是否为空
        if (msgId.isEmpty()) {
            // 错误消息
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.MESSAGE_ID_ERROR);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }

        // 获取用户Id
        String userId = verifyLogin(ctx);
        if (null == userId) {
            // 用户未登录
            return;
        }
        // 判断用户是否在房间中
        String roomId = verifyJoinRoom(ctx);
        if (null == roomId || !roomId.equals(cmd.getRoomId())) {
            // 用户未加入房间
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
        //判断消息是否已发送
        if (messageHolder.isExistMessageId(roomId, msgId)) {
            LOGGER.info("当前消息已发送: msgId = {}", msgId);
            AQChatMsgProtocol.SendMsgAck msgAck = MessageConstructor.buildSendMsgAck(roomId, userId, msgId, cmd.getExt());
            ctx.writeAndFlush(msgAck);
            return;
        }
        //xss过滤
        String clean = SafeUtil.clean(cmd.getMsg());
        // 发送消息
        MessageDto messageDto = new MessageDto();
        messageDto.setMessageId(msgId);
        messageDto.setRoomId(roomId);
        messageDto.setSenderId(userId);
        messageDto.setMessageType(cmd.getMsgType().getNumber());
        messageDto.setMessageContent(clean);
        messageDto.setMessageExt(cmd.getExt());
        messageDto.setCreateTime(new Date());
        mqSendingAgent.sendMessageToRoom(messageDto);
        //判断是否@了AI
        String messageExt = messageDto.getMessageExt();
        if ((AQBusinessConstant.AT + AQBusinessConstant.AI_HELPER_ID).equals(messageExt)) {
            //拷贝一份消息
            MessageDto aiMessageDto = new MessageDto();
            BeanUtils.copyProperties(messageDto, aiMessageDto);
            //覆盖消息Id
            aiMessageDto.setMessageId(IdProvider.nextId()+"");
            //修改消息内容 去掉@AI
            aiMessageDto.setMessageContent(messageDto.getMessageContent().replace((AQBusinessConstant.AT + AQBusinessConstant.AI_HELPER_NAME), ""));
            mqSendingAgent.aiHelper(aiMessageDto);
        }
        mqSendingAgent.storeMessages(messageDto);
        messageHolder.putMessageId(roomId, msgId);
        //返回消息发送成功
        AQChatMsgProtocol.SendMsgAck result = MessageConstructor.buildSendMsgAck(roomId, userId, msgId, cmd.getExt());
        ctx.writeAndFlush(result);
    }
}
