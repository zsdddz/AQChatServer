package com.howcode.aqchat.service.service;


import java.util.List;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.model.MessageRecordDto;

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
     *
     * @param roomId       房间ID
     * @param joinRoomTime
     * @return 房间消息列表
     */
    List<MessageRecordDto> getMessageList(String roomId, Long joinRoomTime);

    void updateMessageVisible(String msgId);
}
