package com.howcode.aqchat.handler.at.chain;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.RoomType;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.common.utils.IdProvider;
import com.howcode.aqchat.handler.at.Handler;
import com.howcode.aqchat.holder.IRoomHolder;
import com.howcode.aqchat.mq.MqSendingAgent;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @Description 多轮对话处理器
 * @Author ZhangWeinan
 * @Date 2024/6/30 17:14
 */
@Component
public class MultipleRoundsHandler extends Handler {
    @Resource
    private IRoomHolder roomHolder;
    @Resource
    private MqSendingAgent mqSendingAgent;

    @Override
    public void handleMessage(MessageDto messageDto) {
        //判断房间类型是否是AI空间
        String roomId = messageDto.getRoomId();
        RoomInfoDto roomInfo = roomHolder.getRoomInfoById(roomId);
        if (RoomType.AI.getCode() ==roomInfo.getRoomType()){
            //拷贝一份消息
            MessageDto aiMessageDto = new MessageDto();
            BeanUtils.copyProperties(messageDto, aiMessageDto);
            //覆盖消息Id
            aiMessageDto.setMessageId(IdProvider.nextId()+"");

            mqSendingAgent.multiple(aiMessageDto);
        }else {
            //继续传递
            if (handler != null) {
                handler.handleMessage(messageDto);
            }
        }
    }
}
