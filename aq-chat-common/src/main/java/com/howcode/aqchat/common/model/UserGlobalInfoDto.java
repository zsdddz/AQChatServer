package com.howcode.aqchat.common.model;

import lombok.Data;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-22 11:39
 */
@Data
public class UserGlobalInfoDto {
    private String userId;
    private String userName;
    private String userAvatar;
    private String roomId;
    //加入房间时间
    private Long joinRoomTime;
}
