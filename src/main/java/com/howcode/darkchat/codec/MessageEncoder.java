package com.howcode.darkchat.codec;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description: 消息编码器
 * @date 2024-04-20 0:38
 */
public class MessageEncoder extends MessageToMessageEncoder<GeneratedMessageV3> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, GeneratedMessageV3 msg, List<Object> list) throws Exception {

    }
}
