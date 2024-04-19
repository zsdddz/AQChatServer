package com.howcode.darkchat.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description: 解码器
 * 消息长度（2位）+消息指令（2位）+消息内容
 * @date 2024-04-19 21:10
 */
public class MessageDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, BinaryWebSocketFrame msg, List<Object> list) throws Exception {
        ByteBuf content = msg.content();
        if (content.readableBytes() < 4) {
            return;
        }
        short msgLength = content.readShort();
        if (content.readableBytes() < msgLength) {
            content.resetReaderIndex();
            return;
        }
        short command = content.readShort();

    }
}
