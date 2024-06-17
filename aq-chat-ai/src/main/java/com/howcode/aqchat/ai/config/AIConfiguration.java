package com.howcode.aqchat.ai.config;

import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.howcode.aqchat.common.config.AQChatConfig;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
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

    @Resource
    private AQChatConfig aqChatConfig;

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

    @Bean
    public GenerationParamHolder generationParamHolder() {
        String apiKey = aqChatConfig.getAiConfig().getApiKey();
        String model = aqChatConfig.getAiConfig().getModel();
        if (apiKey.isEmpty() || model.isEmpty()) {
            LOGGER.info("AI未配置，不启用助手功能");
            return new GenerationParamHolder();
        }
        GenerationParam generationParam = GenerationParam.builder()
                .apiKey(apiKey)
                .model(model)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8)
                .incrementalOutput(true)
                .build();
        GenerationParamHolder generationParamHolder = new GenerationParamHolder();
        generationParamHolder.setGenerationParam(generationParam);
        return generationParamHolder;
    }
}
