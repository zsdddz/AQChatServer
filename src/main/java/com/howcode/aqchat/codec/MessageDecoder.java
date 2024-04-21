package com.howcode.aqchat.codec;


import com.google.protobuf.Message;
import com.howcode.aqchat.message.MessageRecognizer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description: 解码器
 * 消息长度（2位）+消息指令（2位）+消息内容
 * @date 2024-04-19 21:10
 */
@Component
@ChannelHandler.Sharable
public class MessageDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);

    @Resource
    private MessageRecognizer recognizer;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, BinaryWebSocketFrame msg, List<Object> list) throws Exception {
        LOGGER.info("MessageDecoder");
        ByteBuf byteBuf = msg.content();
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        short msgLength = byteBuf.readShort();
        short command = byteBuf.readShort();
        if (byteBuf.readableBytes() < msgLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        Message.Builder builder = recognizer.getMsgBuilderByMsgCommand(command);
        if (builder == null) {
            LOGGER.error("无法识别的消息, command = {}", command);
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        builder.clear();
        builder.mergeFrom(msgBody);

        Message message = builder.build();
        if (message != null) {
            list.add(message);
        }
    }
}
