package com.howcode.aqchat.ai.config;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-10 20:08
 */
@Configuration
public class AIConfiguration implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AIConfiguration.class);
    @Resource
    private RedisCacheHelper redisCacheHelper;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化AI助手信息
        UserGlobalInfoDto aiInfo = new UserGlobalInfoDto();
        aiInfo.setUserId(AQBusinessConstant.AI_HELPER_ID);
        aiInfo.setUserName(AQBusinessConstant.AI_HELPER_NAME);
        aiInfo.setUserAvatar(AQBusinessConstant.AI_HELPER_AVATAR);
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_USER_INFO_PREFIX + AQBusinessConstant.AI_HELPER_ID, aiInfo);
        LOGGER.info("AI助手信息初始化完成");
    }
}
