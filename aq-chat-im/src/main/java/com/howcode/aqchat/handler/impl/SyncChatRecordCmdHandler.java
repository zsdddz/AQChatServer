package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.model.MessageRecordDto;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.service.service.IAQMessageService;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-27 13:26
 */
@Component
public class SyncChatRecordCmdHandler implements ICmdHandler<AQChatMsgProtocol.SyncChatRecordCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncChatRecordCmdHandler.class);
    @Resource
    @Lazy
    private IAQMessageService messageService;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.SyncChatRecordCmd cmd) {
        if (null == ctx || null == cmd) {
            LOGGER.error("SyncChatRecordCmdHandler handle error, ctx or cmd is null");
            return;
        }
        String roomId = cmd.getRoomId();
        List<MessageRecordDto> messageList = messageService.getMessageList(roomId);
        if (null == messageList || messageList.isEmpty()) {
            LOGGER.info("SyncChatRecordCmdHandler handle, messageList is empty");
            return;
        }
        AQChatMsgProtocol.SyncChatRecordAck syncChatRecordAck = MessageConstructor.buildSyncChatRecordAck(messageList);
        ctx.writeAndFlush(syncChatRecordAck);
    }
}
