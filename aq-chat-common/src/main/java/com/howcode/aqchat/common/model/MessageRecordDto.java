package com.howcode.aqchat.common.model;

import lombok.Data;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-27 13:03
 */
@Data
public class MessageRecordDto {
    private Long messageId;
    private String roomId;
    private String senderId;
    private String senderName;
    private String senderAvatar;
    private Integer messageType;
    private String messageContent;
    private String createTime;
}
