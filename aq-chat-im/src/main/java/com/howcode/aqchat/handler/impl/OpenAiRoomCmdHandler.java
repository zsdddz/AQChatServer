package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.ai.config.AIConfiguration;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.enums.RoomType;
import com.howcode.aqchat.common.enums.SwitchStatusEnum;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.common.utils.IdProvider;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IRoomHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 14:54
 */
@Component
public class OpenAiRoomCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.OpenAiRoomCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiRoomCmdHandler.class);

    @Resource
    @Lazy
    private IRoomHolder roomHolder;
    @Resource
    @Lazy
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    @Lazy
    private AIConfiguration aiConfiguration;

    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.OpenAiRoomCmd cmd) {
        //判断是否登录
        String userId = cmd.getUserId();
        if (!verifyLogin(ctx).equals(userId)) {
            return;
        }
        String roomId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).get();
        if (null != roomId) {
            //用户已经在房间中
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_ALREADY_IN_ROOM);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        //创建AI房间
        roomId = IdProvider.generateRoomId();
        ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.ROOM_ID)).set(roomId);

        //将房间信息保存至redis
        RoomInfoDto roomInfoDto = new RoomInfoDto();
        roomInfoDto.setRoomId(roomId);
        roomInfoDto.setRoomType(RoomType.AI.getCode());
        roomInfoDto.setHistory(SwitchStatusEnum.OPEN.getCode());

        //将房间信息保存至redis
        roomHolder.saveRoomInfo(roomId, roomInfoDto);
        //处理房间连接
        globalChannelHolder.createChannelGroup(roomInfoDto.getRoomId());
        //将创建者加入房间
        globalChannelHolder.joinRoom(roomInfoDto.getRoomId(), userId, ctx.channel());
        //添加AI
        roomHolder.saveRoomMember(roomId, AQBusinessConstant.AI_HELPER_ID);
        roomHolder.saveRoomMember(roomId, AQBusinessConstant.XT_ID);
        roomHolder.saveRoomMember(roomId, AQBusinessConstant.XM_ID);
        roomHolder.saveRoomMember(roomId, AQBusinessConstant.XV_ID);


        LOGGER.info("用户{}创建AI房间{}成功", userId, roomInfoDto.getRoomId());
        //返回创建房间成功消息
        List<UserGlobalInfoDto> aiList = aiConfiguration.getAiList();
        AQChatMsgProtocol.OpenAiRoomCmdAck openAiRoomCmdAck = MessageConstructor.buildOpenAiRoomCmdAck(roomId,aiList);
        ctx.writeAndFlush(openAiRoomCmdAck);
    }
}
