package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.common.constant.AQChatConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
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
 * @date 2024-04-22 11:23
 */
@Component
public class JoinRoomCmdHandler implements ICmdHandler<AQChatMsgProtocol.JoinRoomCmd> {

    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    @Lazy
    private RedisCacheHelper redisCacheHelper;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.JoinRoomCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        int roomNo = cmd.getRoomNo();
        //判断房间号是否存在
        String roomId = redisCacheHelper.getCacheObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo, String.class);
        if (null == roomId) {
            //房间号不存在 返回错误信息
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.ROOM_NOT_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        //将用户加入房间
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQChatConstant.AQBusinessConstant.USER_ID)).get();
        globalChannelHolder.joinRoom(roomId, userId, ctx.channel());
        //返回加入房间成功
        //获取房间信息
        RoomInfoDto roomInfoDto = redisCacheHelper.getCacheObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, RoomInfoDto.class);
        AQChatMsgProtocol.JoinRoomAck joinRoomAck = AQChatMsgProtocol.JoinRoomAck.newBuilder()
                .setRoomNo(roomInfoDto.getRoomNo())
                .setRoomName(roomInfoDto.getRoomName())
                .build();
        ctx.writeAndFlush(joinRoomAck);

    }
}
