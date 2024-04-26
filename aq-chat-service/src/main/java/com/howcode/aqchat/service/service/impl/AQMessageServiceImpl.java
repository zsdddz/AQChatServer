package com.howcode.aqchat.service.service.impl;

import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.service.dao.mapper.IAQMessageMapper;
import com.howcode.aqchat.service.dao.po.AqMessage;
import com.howcode.aqchat.service.service.IAQMessageService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-25 23:41
 */
@Service
public class AQMessageServiceImpl implements IAQMessageService {
    @Resource
    private IAQMessageMapper messageMapper;
    @Resource
    private MQProducer mqProducer;

    @Override
    public void saveMessage(MessageDto messageDto) {
        AqMessage aqMessage = new AqMessage();
        aqMessage.setMessageId(messageDto.getMessageId());
        aqMessage.setRoomId(messageDto.getRoomId());
        aqMessage.setSenderId(messageDto.getSenderId());
        aqMessage.setMessageType(messageDto.getMessageType());
        aqMessage.setMessageContent(messageDto.getMessageContent());
        aqMessage.setCreateTime(messageDto.getCreateTime());
        System.out.println(aqMessage);
        messageMapper.insert(aqMessage);
    }

    @Override
    public List<MessageDto> getMessageList(String roomId) {
        return List.of();
    }
}
