package com.howcode.aqchat.message;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-21 23:35
 */
@Component
public class BaseMessageProcessor {

    public <T extends GeneratedMessageV3> void processMessage(NioSocketChannel channel, T message){
        if (channel == null || message == null){
            return;
        }
        channel.writeAndFlush(message);
    }
}
