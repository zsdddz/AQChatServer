package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.IRoomHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-23 12:00
 */
@Component
public class SyncRoomMembersCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.SyncRoomMembersCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncRoomMembersCmdHandler.class);

    @Resource
    private IRoomHolder roomHolder;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.SyncRoomMembersCmd cmd) {
        if (null == ctx || null == cmd) {
            LOGGER.error("SyncRoomMembersCmdHandler handle error, ctx or cmd is null");
            return;
        }
        //判断是否登录  是否有房间
        String userId = verifyLogin(ctx);
        if (null == userId) {
            // 用户未登录
            return;
        }
        String roomId = verifyJoinRoom(ctx);
        if (null == roomId || !roomId.equals(cmd.getRoomId())) {
            // 用户未加入房间
            return;
        }
        RoomInfoDto roomInfoDto = roomHolder.getRoomAllInfoById(roomId);
        AQChatMsgProtocol.SyncRoomMembersAck syncRoomMembersAck = MessageConstructor.buildSyncRoomMembersAck(roomInfoDto);
        ctx.writeAndFlush(syncRoomMembersAck);
    }
}
