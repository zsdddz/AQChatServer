package com.howcode.aqchat.handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: ZhangWeinan
 * @Description: 指令处理器接口
 * @date 2024-04-20 12:15
 */
public interface ICmdHandler<T extends GeneratedMessageV3> {

    /**
     * 处理指令
     *
     * @param ctx 客户端信道处理器上下文
     * @param cmd 指令
     */
    void handle(ChannelHandlerContext ctx, T cmd);

    /**
     * 验证是否登录
     * @param ctx 客户端信道处理器上下文
     */
    String verifyLogin(ChannelHandlerContext ctx);

    /**
     * 验证是否加入房间
     * @param ctx 客户端信道处理器上下文
     */
    String verifyJoinRoom(ChannelHandlerContext ctx);
}
