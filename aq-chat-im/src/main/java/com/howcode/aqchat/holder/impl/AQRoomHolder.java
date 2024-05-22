package com.howcode.aqchat.holder.impl;

import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.common.model.RoomInfoDto;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import com.howcode.aqchat.holder.IRoomHolder;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        //获取房间成员
        List<UserGlobalInfoDto> roomMembers = getRoomMembers(roomId);
        roomInfo.setRoomMembers(roomMembers);
        return roomInfo;
    }

    @Override
    public void removeRoomInfo(Integer roomNo) {
        String roomId = getRoomId(roomNo);
        redisCacheHelper.deleteObject(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId);
        redisCacheHelper.deleteObject(AQRedisKeyPrefix.AQ_ROOM_NO_PREFIX + roomNo);
    }

    @Override
    public void saveRoomMember(String roomId, String userId) {
        //判断map是否存在
        Map<String, UserGlobalInfoDto> roomMembers = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX);
        if (null == roomMembers) {
            //不存在则创建
            roomMembers = new HashMap<>();
            UserGlobalInfoDto userInfo = redisCacheHelper.getCacheObject(AQRedisKeyPrefix.AQ_USER_INFO_PREFIX + userId, UserGlobalInfoDto.class);
            roomMembers.put(userId, userInfo);
            redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX, roomMembers);
        } else {
            //存在则添加
            UserGlobalInfoDto userInfo = redisCacheHelper.getCacheObject(AQRedisKeyPrefix.AQ_USER_INFO_PREFIX + userId, UserGlobalInfoDto.class);
            roomMembers.put(userId, userInfo);
            redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX, roomMembers);
        }
    }

    @Override
    public List<UserGlobalInfoDto> getRoomMembers(String roomId) {
        Map<String, UserGlobalInfoDto> roomMembers = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MEMBER_PREFIX);
        if (null == roomMembers) {
            LOGGER.error("[获取房间成员]房间成员不存在");
            return null;
        }
        return (List<UserGlobalInfoDto>) roomMembers.values();
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
}
