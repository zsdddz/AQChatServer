package com.howcode.aqchat.common.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-24 1:00
 */
@Data
public class RoomInfoDto {
    private String roomId;
    private Integer roomNo;
    private String roomName;
    private List<UserGlobalInfoDto> roomMembers;
}
