package com.howcode.aqchat.service;

import com.howcode.aqchat.model.MessageDto;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description: 负责储存消息、根据房间ID获取当前房间消息列表
 * @date 2024-04-25 23:36
 */
public interface IAQMessageService {

    /**
     * 存储消息
     * @param messageDto 消息对象
     */
    void saveMessage(MessageDto messageDto);

    /**
     * 获取房间消息列表
     * @param roomId 房间ID
     * @return 房间消息列表
     */
    List<MessageDto> getMessageList(String roomId);

}
