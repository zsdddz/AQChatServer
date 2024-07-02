package com.howcode.aqchat.mq.receive;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AISpaceStatusEnum;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.holder.IRoomHolder;
import jakarta.annotation.Resource;

import java.util.Date;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/7/2 11:12
 */

public abstract class AbstractAISpaceReceiver {
    @Resource
    protected IRoomHolder roomHolder;

    // AI空间消息完成后置处理
    protected void afterAISpaceMessage(String roomId) {
        roomHolder.setAISpaceStatus(roomId, AISpaceStatusEnum.IDLE.getCode());
    }


    protected static MessageDto buildStoreMessageByFile(MessageDto messageDto, StringBuilder fullContent, Integer msgType, String userId) {
        MessageDto storeMessage = new MessageDto();
        storeMessage.setMessageId(messageDto.getMessageId());
        storeMessage.setMessageType(msgType);
        storeMessage.setSenderId(userId);
        storeMessage.setRoomId(messageDto.getRoomId());
        storeMessage.setCreateTime(new Date());
        int lastIndex = fullContent.toString().lastIndexOf('/');
        if (lastIndex != -1) {
            String fileName = fullContent.substring(lastIndex + 1);
            storeMessage.setMessageExt(fileName);
        }
        storeMessage.setMessageContent(fullContent.toString());
        return storeMessage;
    }

    protected static MessageDto buildStoreMessage(MessageDto messageDto, StringBuilder fullContent, Integer msgType, String senderId) {
        MessageDto storeMessage = new MessageDto();
        storeMessage.setMessageId(messageDto.getMessageId());
        storeMessage.setMessageType(msgType);
        storeMessage.setSenderId(senderId);
        storeMessage.setRoomId(messageDto.getRoomId());
        storeMessage.setCreateTime(new Date());
        storeMessage.setMessageExt(AQBusinessConstant.AT + messageDto.getSenderId());
        storeMessage.setMessageContent(fullContent.toString());
        return storeMessage;
    }
}
