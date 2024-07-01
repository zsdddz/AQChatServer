package com.howcode.aqchat.holder;

import com.howcode.aqchat.ai.parameter.MessageRecord;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;

import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-22 18:35
 */
public interface IRoomHolder {

    /**
     * 保存房间信息
     * @param roomId 房间ID
     * @param roomInfoDto 房间信息
     */
    void saveRoomInfo(String roomId, RoomInfoDto roomInfoDto);

    void saveNoAndId(Integer roomNo, String roomId);

    String getRoomId(Integer roomNo);

    /**
     * 根据房间号获取房间信息
     * @param roomNo 房间号
     */
    RoomInfoDto getRoomInfoByNo(Integer roomNo);
    /**
     * 根据房间id获取房间信息
     * @param roomId 房间id
     */
    RoomInfoDto getRoomInfoById(String roomId);

    /**
     * 根据房间id获取房间全部信息
     * @param roomId 房间id
     */
    RoomInfoDto getRoomAllInfoById(String roomId);

    /**
     * 兼容接口 判断是否过滤用户
     */
    RoomInfoDto getRoomAllInfoById(String roomId,String userId);

    /**
     * 删除房间信息
     * @param roomNo 房间号
     */
    void removeRoomInfo(Integer roomNo);
    void removeRoomInfo(String roomId);

    /**
     * 保存房间成员
     * @param roomId 房间id
     * @param userId 用户id
     */
    void saveRoomMember(String roomId, String userId);


    /**
     * 获取房间成员
     * @param roomId 房间id
     */
    List<UserGlobalInfoDto> getRoomMembers(String roomId);

    /**
     * 删除房间成员
     * @param roomId 房间id
     * @param userId 用户id
     */
    void removeRoomMember(String roomId, String userId);

    List<MessageRecord> getRoomConversationRecords(String roomId);

    void addRoomConversationRecord(String roomId, MessageRecord userRecord);
}
