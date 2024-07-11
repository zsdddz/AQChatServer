package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.mq.MqSendingAgent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-24 14:06
 */
@Component
public class LeaveRoomCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.LeaveRoomCmd> {
    @Resource
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    private MqSendingAgent mqSendingAgent;
    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.LeaveRoomCmd cmd) {
        if (ctx == null || cmd == null) {
            return;
        }
        // 验证用户是否登录
        String userId = verifyLogin(ctx);
        if (null == userId) {
            // 用户未登录
            return;
        }
        // 获取房间Id
        String roomId = verifyJoinRoom(ctx);
        String cmdRoomId = cmd.getRoomId();
        if (null == roomId || !roomId.equals(cmdRoomId)) {
            // 用户不在房间中
            return;
        }
        // 判断房间是否存在
        RoomInfoDto roomInfoDto = globalChannelHolder.getRoomInfo(cmdRoomId);
        if (null == roomInfoDto) {
            // 房间不存在
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.ROOM_NOT_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).set(null);
        globalChannelHolder.leaveRoom(userId,ctx.channel());
        mqSendingAgent.sendLeaveRoomMsg(userId,cmdRoomId);
        //判断房间是否为空 如果为空则解散房间
        globalChannelHolder.dissolveTheRoomByLogout(cmdRoomId);
        //返回离开房间成功
        AQChatMsgProtocol.LeaveRoomAck leaveRoomAck = AQChatMsgProtocol.LeaveRoomAck
                .newBuilder()
                .setRoomId(cmdRoomId)
                .build();
        ctx.writeAndFlush(leaveRoomAck);
    }
}
