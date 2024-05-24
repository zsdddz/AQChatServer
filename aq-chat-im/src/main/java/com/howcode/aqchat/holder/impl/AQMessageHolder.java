package com.howcode.aqchat.holder.impl;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import com.howcode.aqchat.holder.IMessageHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
    public void putMessageId(long msgId) {
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_MESSAGE_ID + msgId,
                0,
                AQBusinessConstant.MESSAGE_ID_CACHE_TIME,
                TimeUnit.SECONDS);
    }

    @Override
    public boolean isExistMessageId(long msgId) {
        return null != redisCacheHelper.getCacheObject(AQRedisKeyPrefix.AQ_MESSAGE_ID + msgId, Integer.class);
    }
}
