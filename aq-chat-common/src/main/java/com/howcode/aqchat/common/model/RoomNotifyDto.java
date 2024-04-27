package com.howcode.aqchat.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-27 11:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomNotifyDto {
    private String roomId;
    private String userId;
}
