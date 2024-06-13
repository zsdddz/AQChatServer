package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.mq.MqSendingAgent;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-26 18:24
 */
@Component
public class RecallMsgCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.RecallMsgCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecallMsgCmdHandler.class);
    @Resource
    @Lazy
    private MqSendingAgent mqSendingAgent;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.RecallMsgCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        String userId = verifyLogin(ctx);
        if (null == userId) {
            return;
        }
        String roomId = verifyJoinRoom(ctx);
        if (null == roomId || !roomId.equals(cmd.getRoomId())) {
            // 用户未加入房间
            LOGGER.warn("[非法操作] 用户使用非法参数");
            return;
        }
        String msgId = cmd.getMsgId();
        mqSendingAgent.sendRecallMessage(roomId, msgId, userId);
        AQChatMsgProtocol.RecallMsgAck ack = MessageConstructor.buildRecallMsgAck(msgId, roomId, userId);
        ctx.writeAndFlush(ack);
    }
}
