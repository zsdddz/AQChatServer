package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IRoomHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 14:56
 */
@Component
public class CloseAiRoomCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.CloseAiRoomCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseAiRoomCmdHandler.class);

    @Resource
    @Lazy
    private IRoomHolder roomHolder;
    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.CloseAiRoomCmd cmd) {
        //判断是否登录
        if (verifyLogin(ctx).isEmpty()) {
            return;
        }
        String roomId = verifyJoinRoom(ctx);
        if (roomId == null||roomId.equals(cmd.getRoomId())) {
            return;
        }
        globalChannelHolder.dissolveTheRoomByLogout(roomId);
        //关闭房间
        roomHolder.removeRoomInfo(roomId);
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).set(null);
        //返回关闭房间成功
        AQChatMsgProtocol.CloseAiRoomCmdAck result = AQChatMsgProtocol.CloseAiRoomCmdAck.newBuilder()
                .setRoomId(roomId)
                .build();
        ctx.writeAndFlush(result);
    }
}
