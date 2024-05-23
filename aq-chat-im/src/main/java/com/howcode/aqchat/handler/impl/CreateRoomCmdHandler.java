package com.howcode.aqchat.handler.impl;


import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.common.utils.IdProvider;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IRoomHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.mq.MqSendingAgent;
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
public class CreateRoomCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.CreateRoomCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateRoomCmdHandler.class);

    @Resource
    @Lazy
    private IRoomHolder roomHolder;

    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    @Lazy
    private MqSendingAgent mqSendingAgent;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.CreateRoomCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        //判断用户是否登录
        String userId = verifyLogin(ctx);
        if (null == userId) {
            LOGGER.error("CreateRoomCmdHandler handle error, user not login");
            return;
        }
        String roomId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).get();
        if (null != roomId) {
            //用户已经在房间中
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_ALREADY_IN_ROOM);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        String roomName = cmd.getRoomName();
        int roomNo = cmd.getRoomNo();
        //判断当前房间号是否已经存在
        String roomNoIsExist = roomHolder.getRoomId(roomNo);
        if (null != roomNoIsExist) {
            //房间号已经存在 返回错误信息
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.ROOM_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        //将房间号保存至redis
        roomId = IdProvider.generateRoomId();
        roomHolder.saveNoAndId(roomNo, roomId);
        //将房间信息保存至redis
        RoomInfoDto roomInfoDto = new RoomInfoDto();
        roomInfoDto.setRoomId(roomId);
        roomInfoDto.setRoomNo(roomNo);
        roomInfoDto.setRoomName(roomName);
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).set(roomId);
        //将房间信息保存至redis
        roomHolder.saveRoomInfo(roomId, roomInfoDto);
        //处理房间连接
        globalChannelHolder.createChannelGroup(roomInfoDto.getRoomId());
        //将创建者加入房间
        globalChannelHolder.joinRoom(roomInfoDto.getRoomId(), userId, ctx.channel());
        //mq发送加入房间消息
        mqSendingAgent.sendJoinRoomMsg(userId, roomInfoDto.getRoomId());

        LOGGER.info("用户{}创建房间{}成功", userId, roomInfoDto.getRoomId());
        //返回创建房间成功消息
        AQChatMsgProtocol.CreateRoomAck createRoomAck = AQChatMsgProtocol.CreateRoomAck.newBuilder()
                .setRoomId(roomInfoDto.getRoomId())
                .setRoomName(roomName)
                .setRoomNo(roomNo)
                .build();
        ctx.writeAndFlush(createRoomAck);
    }
}
