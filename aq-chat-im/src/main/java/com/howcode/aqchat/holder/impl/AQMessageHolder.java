package com.howcode.aqchat.holder.impl;

import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import com.howcode.aqchat.holder.IMessageHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-24 10:34
 */
@Component
public class AQMessageHolder implements IMessageHolder {

    @Resource
    private RedisCacheHelper redisCacheHelper;

    @Override
    public void putMessageId(String roomId, String msgId) {
        List<String> messageIds = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId,AQRedisKeyPrefix.AQ_ROOM_MESSAGE_PREFIX);
        if (null == messageIds) {
            messageIds = new ArrayList<>();
            messageIds.add(msgId);
        } else {
            messageIds.add(msgId);
        }
        redisCacheHelper.setCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId, AQRedisKeyPrefix.AQ_ROOM_MESSAGE_PREFIX, messageIds);
    }

    @Override
    public boolean isExistMessageId(String roomId, String msgId) {
        List<String> messageIds = redisCacheHelper.getCacheMapValue(AQRedisKeyPrefix.AQ_ROOM_PREFIX + roomId,AQRedisKeyPrefix.AQ_ROOM_MESSAGE_PREFIX);
        if (null == messageIds) {
            return false;
        }
        return messageIds.contains(msgId);
    }
}
