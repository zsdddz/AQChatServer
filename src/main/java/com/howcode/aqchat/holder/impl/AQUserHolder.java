package com.howcode.aqchat.holder.impl;

import com.howcode.aqchat.constant.AQChatConstant;
import com.howcode.aqchat.holder.IUserHolder;
import com.howcode.aqchat.model.UserGlobalInfoDto;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, UserGlobalInfoDto> redisTemplate;

    @Override
    public void saveUserLoginInfo(UserGlobalInfoDto userGlobalInfoDto) {
        redisTemplate.opsForValue().set(AQChatConstant.AQRedisKeyPrefix.AQ_USER_LOGIN_INFO_PREFIX + userGlobalInfoDto.getUserId(),
                userGlobalInfoDto,
                AQChatConstant.AQBusinessConstant.USER_INFO_CACHE_TIME,
                TimeUnit.SECONDS);
    }

    @Override
    public UserGlobalInfoDto getUserLoginInfo(String userId) {
        return redisTemplate.opsForValue().get(AQChatConstant.AQRedisKeyPrefix.AQ_USER_LOGIN_INFO_PREFIX + userId);
    }

    @Override
    public void removeUserLoginInfo(String userId) {
        redisTemplate.delete(AQChatConstant.AQRedisKeyPrefix.AQ_USER_LOGIN_INFO_PREFIX + userId);
    }
}
