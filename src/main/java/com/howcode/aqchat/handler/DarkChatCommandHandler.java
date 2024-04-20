package com.howcode.aqchat.handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-20 0:35
 */
@Component
@ChannelHandler.Sharable
public class DarkChatCommandHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DarkChatCommandHandler.class);

    @Resource
    CommandHandlerFactory commandHandlerFactory;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof GeneratedMessageV3){
            ICmdHandler<? extends GeneratedMessageV3> commandHandler = commandHandlerFactory.getCommandHandler(msg.getClass());
            if (commandHandler != null){
                commandHandler.handle(channelHandlerContext, castToMessage(msg));
            }
        }
    }

    private <T extends GeneratedMessageV3> T castToMessage(Object msg){
        if (msg == null){
            return null;
        }
        return (T) msg;
    }
}