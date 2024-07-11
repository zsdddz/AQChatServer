package com.howcode.aqchat.holder.impl;

import com.howcode.aqchat.ai.parameter.MessageRecord;
import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.common.enums.RoomType;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import com.howcode.aqchat.holder.IRoomHolder;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-22 18:39
 */
@Component
public class AQRoomHolder implements IRoomHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(AQRoomHolder.class);

    @Resource
    private RedisCacheHelper redisCacheHelper;

    @Override
    public void saveRoomInfo(String roomId, RoomInfoDto roomInfoDto) {
        redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomInfoDto.getRoomId(), AQRedisKeyPrefix.AQ_ROOM_INFO_PREFIX, roomInfoDto);
    }

    @Override
    public void saveNoAndId(Integer roomNo, String roomId) {
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo, roomId);
    }

    @Override
    public String getRoomId(Integer roomNo) {
        return redisCacheHelper.getCacheObject(AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo, String.class);
    }

    @Override
    public RoomInfoDto getRoomInfoByNo(Integer roomNo) {
        return redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + getRoomId(roomNo), AQRedisKeyPrefix.AQ_ROOM_INFO_PREFIX);
    }

    @Override
    public RoomInfoDto getRoomInfoById(String roomId) {
        RoomInfoDto roomInfo = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_INFO_PREFIX);
        if (null == roomInfo) {
            LOGGER.error("[获取房间信息]房间不存在");
            return null;
        }
        return roomInfo;
    }

    @Override
    public RoomInfoDto getRoomAllInfoById(String roomId) {
        RoomInfoDto roomInfo = getRoomInfoById(roomId);
        if (null == roomInfo) {
            return null;
        }
        //获取房间成员
        List<UserGlobalInfoDto> roomMembers = getRoomMembers(roomId);
        roomInfo.setRoomMembers(roomMembers);
        return roomInfo;
    }

    @Override
    public RoomInfoDto getRoomAllInfoById(String roomId, String userId) {
        RoomInfoDto roomInfo = getRoomAllInfoById(roomId);
        if (null == roomInfo) {
            return null;
        }
        if (RoomType.AI.getCode() == roomInfo.getRoomType()) {
            //获取房间成员
            List<UserGlobalInfoDto> roomMembers = roomInfo.getRoomMembers();
            //过滤自己
            roomMembers.removeIf(userGlobalInfoDto -> userGlobalInfoDto.getUserId().equals(userId));
            roomInfo.setRoomMembers(roomMembers);
        }
        return roomInfo;
    }

    @Override
    public void removeRoomInfo(Integer roomNo) {
        String roomId = getRoomId(roomNo);
        redisCacheHelper.deleteObject(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId);
        redisCacheHelper.deleteObject(AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo);
    }

    @Override
    public void removeRoomInfo(String roomId) {
        redisCacheHelper.deleteObject(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId);
    }

    @Override
    public void saveRoomMember(String roomId, String userId) {
        //判断map是否存在
        Map<String, UserGlobalInfoDto> roomMembers = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX);
        UserGlobalInfoDto userInfo = redisCacheHelper.getCacheObject(AQRedisKeyPrefix.AQ_USER_INFO_PREFIX + userId, UserGlobalInfoDto.class);
        if (null == roomMembers && null != userInfo) {
            //不存在则创建
            roomMembers = new HashMap<>();
            roomMembers.put(userId, userInfo);
            redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX, roomMembers);
        } else if (null != userInfo) {
            //存在则添加
            roomMembers.put(userId, userInfo);
            redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX, roomMembers);
        } else {
            LOGGER.error("[保存房间成员]用户信息不存在");
        }
    }

    @Override
    public List<UserGlobalInfoDto> getRoomMembers(String roomId) {
        Map<String, UserGlobalInfoDto> roomMembers = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX);
        if (null == roomMembers) {
            LOGGER.error("[获取房间成员]房间成员不存在");
            return null;
        }
        List<UserGlobalInfoDto> userGlobalInfoDtoList = new ArrayList<>();
        for (Map.Entry<String, UserGlobalInfoDto> entry : roomMembers.entrySet()) {
            userGlobalInfoDtoList.add(entry.getValue());
        }
        return userGlobalInfoDtoList;
    }

    @Override
    public void removeRoomMember(String roomId, String userId) {
        //获取房间ID
        if (null == roomId) {
            LOGGER.error("[移除成员]房间不存在");
            return;
        }
        //获取房间成员
        Map<String, UserGlobalInfoDto> roomMembers = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX);
        if (null == roomMembers) {
            LOGGER.error("[移除成员]房间成员不存在");
            return;
        }
        //移除成员
        roomMembers.remove(userId);
        redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX, roomMembers);
    }

    @Override
    public List<MessageRecord> getRoomConversationRecords(String roomId) {
        return redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_CONVERSATION_PREFIX);

    }

    @Override
    public void addRoomConversationRecord(String roomId, MessageRecord userRecord) {
        List<MessageRecord> messageRecords = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_CONVERSATION_PREFIX);
        if (null == messageRecords && null != userRecord) {
            messageRecords = new ArrayList<>();
            messageRecords.add(userRecord);
            redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_CONVERSATION_PREFIX, messageRecords);
        } else if (null != messageRecords && null != userRecord) {
            messageRecords.add(userRecord);
            redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_CONVERSATION_PREFIX, messageRecords);
        }else {
            LOGGER.error("[添加房间消息记录]消息为空");
        }
    }

    @Override
    public int getAISpaceStatus(String roomId) {
        Object status = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_AI_SPACE_STATUS_PREFIX);
        if (null == status) {
            return 0;
        }
        return (int) status;
    }

    @Override
    public void setAISpaceStatus(String roomId, int code) {
        redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_AI_SPACE_STATUS_PREFIX, code);
    }
}
