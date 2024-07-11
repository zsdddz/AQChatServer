package com.howcode.aqchat.common.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description: 房间缓存信息
 * @date 2024-04-24 1:00
 */
@Data
public class RoomInfoDto {
    private String roomId;
    private Integer roomNo;
    private String roomName;
    //房间类型 0普通房间 1AI房间
    private int roomType;
    //是否支持历史消息 0不支持 1支持
    private int history;
    private int ai;
    private List<UserGlobalInfoDto> roomMembers;
}
