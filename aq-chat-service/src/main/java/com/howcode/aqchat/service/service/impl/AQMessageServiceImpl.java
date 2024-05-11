package com.howcode.aqchat.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.model.MessageRecordDto;
import com.howcode.aqchat.service.dao.mapper.IAQMessageMapper;
import com.howcode.aqchat.service.dao.po.AqMessage;
import com.howcode.aqchat.service.dao.po.AqUser;
import com.howcode.aqchat.service.service.IAQMessageService;
import com.howcode.aqchat.service.service.IAQUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private IAQUserService userService;

    @Override
    public void saveMessage(MessageDto messageDto) {
        AqMessage aqMessage = new AqMessage();
        aqMessage.setMessageId(messageDto.getMessageId());
        aqMessage.setRoomId(messageDto.getRoomId());
        aqMessage.setSenderId(messageDto.getSenderId());
        aqMessage.setMessageType(messageDto.getMessageType());
        aqMessage.setMessageContent(messageDto.getMessageContent());
        aqMessage.setCreateTime(messageDto.getCreateTime());
        messageMapper.insert(aqMessage);
    }

    @Override
    public List<MessageRecordDto> getMessageList(String roomId) {
        //查询房间最后100条消息
        LambdaQueryWrapper<AqMessage> query = new LambdaQueryWrapper<AqMessage>().eq(AqMessage::getRoomId, roomId)
                .orderByDesc(AqMessage::getCreateTime)
                .last(AQBusinessConstant.LIMIT);
        List<AqMessage> aqMessages = messageMapper.selectList(query);
        return convertMessageList(aqMessages);
    }

    private List<MessageRecordDto> convertMessageList(List<AqMessage> aqMessages) {
        //格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //缓存用户信息
        Map<String, AqUser> userMap = new HashMap<>();

        List<MessageRecordDto> collect = aqMessages.stream().map(m -> {
            MessageRecordDto messageRecordDto = new MessageRecordDto();
            messageRecordDto.setMessageId(m.getMessageId().toString());
            messageRecordDto.setRoomId(m.getRoomId());
            messageRecordDto.setSenderId(m.getSenderId());

            AqUser aqUser = userMap.get(m.getSenderId());
            if (aqUser == null) {
                aqUser = userService.getUserById(m.getSenderId());
                if (aqUser == null) {
                    aqUser = new AqUser("","","");
                }
                userMap.put(m.getSenderId(), aqUser);
            }
            messageRecordDto.setSenderName(aqUser.getUserName());
            messageRecordDto.setSenderAvatar(aqUser.getUserAvatar());
            messageRecordDto.setMessageType(m.getMessageType());
            messageRecordDto.setMessageContent(m.getMessageContent());
            messageRecordDto.setCreateTime(sdf.format(m.getCreateTime()));
            return messageRecordDto;
        }).collect(Collectors.toList());
        return collect;
    }
}
