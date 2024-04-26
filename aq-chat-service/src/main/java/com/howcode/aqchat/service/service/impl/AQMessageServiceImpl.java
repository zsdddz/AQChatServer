package com.howcode.aqchat.service.service.impl;

import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.service.dao.mapper.IAQMessageMapper;
import com.howcode.aqchat.service.service.IAQMessageService;
import jakarta.annotation.Resource;
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

    @Override
    public void saveMessage(MessageDto messageDto) {

    }

    @Override
    public List<MessageDto> getMessageList(String roomId) {
        return List.of();
    }
}
