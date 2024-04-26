package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-24 14:06
 */
@Component
public class LeaveRoomCmdHandler implements ICmdHandler<AQChatMsgProtocol.LeaveRoomCmd> {
    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.LeaveRoomCmd cmd) {
        if (ctx == null || cmd == null) {
            return;
        }
        // 获取用户Idq
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).get();
        if (null == userId) {
            // 用户未登录
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_NOT_LOGIN);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        // 获取房间Id
        String roomId = cmd.getRoomId();
        // 判断房间是否存在
        RoomInfoDto roomInfoDto = globalChannelHolder.getRoomInfo(roomId);
        if (null == roomInfoDto) {
            // 房间不存在
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.ROOM_NOT_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        globalChannelHolder.leaveRoom(userId,ctx.channel());

        //判断房间是否为空 如果为空则解散房间
        globalChannelHolder.isOrNoDissolveTheRoom(roomId,roomInfoDto.getRoomNo());
        //返回离开房间成功
        AQChatMsgProtocol.LeaveRoomAck.Builder builder = AQChatMsgProtocol.LeaveRoomAck.newBuilder();
        builder.setRoomId(roomId);
        ctx.writeAndFlush(builder.build());
    }
}
