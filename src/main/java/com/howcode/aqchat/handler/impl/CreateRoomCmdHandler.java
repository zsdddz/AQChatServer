package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.constant.AQChatConstant;
import com.howcode.aqchat.enums.AQChatExceptionEnum;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.model.RoomInfoDto;
import com.howcode.aqchat.utils.IdProvider;
import com.howcode.aqchat.utils.RedisCacheHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-23 23:48
 */
@Component
public class CreateRoomCmdHandler implements ICmdHandler<AQChatMsgProtocol.CreateRoomCmd> {

    @Resource
    @Lazy
    private RedisCacheHelper redisCacheHelper;

    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.CreateRoomCmd cmd) {
        if (null == ctx || null == cmd){
            return;
        }
        String roomName = cmd.getRoomName();
        int roomNo = cmd.getRoomNo();
        //判断当前房间号是否已经存在
        String roomNoIsExist = redisCacheHelper.getCacheObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo,String.class);
        if (null != roomNoIsExist){
            //房间号已经存在 返回错误信息
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.ROOM_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        //将房间号保存至redis
        String roomId = IdProvider.generateRoomId();
        redisCacheHelper.setCacheObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo,roomId);
        //将房间信息保存至redis
        RoomInfoDto roomInfoDto = new RoomInfoDto();
        roomInfoDto.setRoomId(roomId);
        roomInfoDto.setRoomNo(roomNo);
        roomInfoDto.setRoomName(roomName);
        redisCacheHelper.setCacheObject(AQChatConstant.AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomInfoDto.getRoomId(),roomInfoDto);
        //处理房间连接
        globalChannelHolder.createChannelGroup(roomInfoDto.getRoomId());
        //将创建者加入房间
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQChatConstant.AQBusinessConstant.USER_ID)).get();
        globalChannelHolder.joinRoom(roomInfoDto.getRoomId(),userId,ctx.channel());
        //返回创建房间成功消息
        AQChatMsgProtocol.CreateRoomAck createRoomAck = AQChatMsgProtocol.CreateRoomAck.newBuilder()
                .setRoomId(roomInfoDto.getRoomId())
                .setRoomName(roomName)
                .setRoomNo(roomNo)
                .build();
        ctx.writeAndFlush(createRoomAck);
    }
}
