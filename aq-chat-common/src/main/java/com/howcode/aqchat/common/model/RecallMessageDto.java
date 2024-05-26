package com.howcode.aqchat.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-26 18:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecallMessageDto {
    private String roomId;
    private Long msgId;
    private String userId;
}
