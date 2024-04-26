package com.howcode.aqchat.handler.impl;


import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.common.utils.IdProvider;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-23 23:48
 */
@Component
public class CreateRoomCmdHandler implements ICmdHandler<AQChatMsgProtocol.CreateRoomCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateRoomCmdHandler.class);

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
        String roomNoIsExist = redisCacheHelper.getCacheObject(AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo,String.class);
        if (null != roomNoIsExist){
            //房间号已经存在 返回错误信息
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.ROOM_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        //将房间号保存至redis
        String roomId = IdProvider.generateRoomId();
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo,roomId);
        //将房间信息保存至redis
        RoomInfoDto roomInfoDto = new RoomInfoDto();
        roomInfoDto.setRoomId(roomId);
        roomInfoDto.setRoomNo(roomNo);
        roomInfoDto.setRoomName(roomName);
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomInfoDto.getRoomId(),roomInfoDto);
        //处理房间连接
        globalChannelHolder.createChannelGroup(roomInfoDto.getRoomId());
        //将创建者加入房间
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).get();
        globalChannelHolder.joinRoom(roomInfoDto.getRoomId(),userId,ctx.channel());
        LOGGER.info("用户{}创建房间{}成功",userId,roomInfoDto.getRoomId());
        //返回创建房间成功消息
        AQChatMsgProtocol.CreateRoomAck createRoomAck = AQChatMsgProtocol.CreateRoomAck.newBuilder()
                .setRoomId(roomInfoDto.getRoomId())
                .setRoomName(roomName)
                .setRoomNo(roomNo)
                .build();
        ctx.writeAndFlush(createRoomAck);
    }
}
