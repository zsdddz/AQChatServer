package com.howcode.aqchat.holder.impl;


import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import com.howcode.aqchat.holder.IUserHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-22 11:53
 */
@Component
public class AQUserHolder implements IUserHolder {

    @Resource
    private RedisCacheHelper redisCacheHelper;

    @Override
    public void saveUserLoginInfo(UserGlobalInfoDto userGlobalInfoDto) {
        redisCacheHelper.setCacheObject(
                AQRedisKeyPrefix.AQ_USER_LOGIN_INFO_PREFIX + userGlobalInfoDto.getUserId(),
                userGlobalInfoDto,
                AQBusinessConstant.USER_INFO_CACHE_TIME,
                TimeUnit.SECONDS);
    }

    @Override
    public UserGlobalInfoDto getUserLoginInfo(String userId) {
        return redisCacheHelper.getCacheObject(AQRedisKeyPrefix.AQ_USER_LOGIN_INFO_PREFIX + userId,UserGlobalInfoDto.class);
    }

    @Override
    public void removeUserLoginInfo(String userId) {
        redisCacheHelper.deleteObject(AQRedisKeyPrefix.AQ_USER_LOGIN_INFO_PREFIX + userId);
    }

    @Override
    public void offline(String userId) {
        redisCacheHelper.expire(AQRedisKeyPrefix.AQ_USER_LOGIN_INFO_PREFIX + userId,
                AQBusinessConstant.USER_OFFLINE_CACHE_TIME,
                TimeUnit.SECONDS);
    }
}
