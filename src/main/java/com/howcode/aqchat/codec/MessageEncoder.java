package com.howcode.aqchat.codec;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
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
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, GeneratedMessageV3 msg, List<Object> list) throws Exception {

    }
}
