package com.howcode.aqchat.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-25 23:39
 */
@Data
public class MessageDto {
    private Long messageId;
    private String roomId;
    private String senderId;
    private Integer messageType;
    private String messageContent;
    private Date createTime;
}
