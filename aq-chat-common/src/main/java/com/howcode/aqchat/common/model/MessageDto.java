package com.howcode.aqchat.common.model;

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
    private String messageExt;
    private Date createTime;
}
