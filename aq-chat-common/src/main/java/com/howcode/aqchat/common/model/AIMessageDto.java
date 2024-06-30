package com.howcode.aqchat.common.model;

import lombok.Data;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-10 18:48
 */
@Data
public class AIMessageDto {
    private String messageId;
    private String roomId;
    private String content;
    private int msgType;
    private int status;
}
