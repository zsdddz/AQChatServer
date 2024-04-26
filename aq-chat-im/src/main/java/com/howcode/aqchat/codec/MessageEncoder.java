package com.howcode.aqchat.codec;

import com.google.protobuf.GeneratedMessageV3;
import com.howcode.aqchat.message.MessageRecognizer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description: 消息编码器
 * @date 2024-04-20 0:38
 */
@Component
@ChannelHandler.Sharable
public class MessageEncoder extends MessageToMessageEncoder<GeneratedMessageV3> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);
    @Resource
    private MessageRecognizer recognizer;

    @Override
    protected void encode(ChannelHandlerContext ctx, GeneratedMessageV3 msg, List<Object> list) throws Exception {
        int msgCommand = recognizer.getMsgCommandByMsgClazz(msg.getClass());
        if (msgCommand < 0) {
            LOGGER.error("无法识别的消息, msgClazz = {}", msg.getClass().getSimpleName());
            return;
        }
        LOGGER.info("返回消息编码器: msgClazz = {}, msgCommand = {}", msg.getClass().getSimpleName(), msgCommand);
        byte[] msgBody = msg.toByteArray();
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeShort((short) msgBody.length);
        byteBuf.writeShort((short) msgCommand);
        byteBuf.writeBytes(msgBody);
        list.add(new BinaryWebSocketFrame(byteBuf));
    }
}
