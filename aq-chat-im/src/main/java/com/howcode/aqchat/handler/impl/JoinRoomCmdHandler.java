package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IRoomHolder;
import com.howcode.aqchat.holder.IUserHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.mq.MqSendingAgent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-22 11:23
 */
@Component
public class JoinRoomCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.JoinRoomCmd> {

    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    @Lazy
    private IRoomHolder roomHolder;

    @Resource
    @Lazy
    private MqSendingAgent mqSendingAgent;
    @Resource
    @Lazy
    private IUserHolder userHolder;


    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.JoinRoomCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        // 获取用户Id
        String userId = verifyLogin(ctx);
        if (null == userId) {
            // 用户未登录
            return;
        }
        // 判断用户是否已经在房间中
        String roomId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).get();
        if (null != roomId) {
            //用户已经在房间中
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_ALREADY_IN_ROOM);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }

        int roomNo = cmd.getRoomNo();
        //判断房间号是否存在
        roomId = roomHolder.getRoomId(roomNo);
        if (null == roomId) {
            //房间号不存在 返回错误信息
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.ROOM_NOT_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).set(roomId);
        //将用户加入房间
        globalChannelHolder.joinRoom(roomId, userId, ctx.channel());
        userHolder.setJoinRoomTime(userId, System.currentTimeMillis());
        mqSendingAgent.sendJoinRoomMsg(userId, roomId);
        //返回加入房间成功
        RoomInfoDto roomInfoDto = globalChannelHolder.getRoomInfo(roomId);
        AQChatMsgProtocol.JoinRoomAck joinRoomAck = AQChatMsgProtocol.JoinRoomAck.newBuilder()
                .setRoomId(roomId)
                .setRoomNo(roomInfoDto.getRoomNo())
                .setRoomName(roomInfoDto.getRoomName())
                .setAi(roomInfoDto.getAi())
                .build();
        ctx.writeAndFlush(joinRoomAck);

    }
}
