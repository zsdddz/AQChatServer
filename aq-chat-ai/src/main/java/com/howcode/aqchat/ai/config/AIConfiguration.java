package com.howcode.aqchat.ai.config;

import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.howcode.aqchat.common.config.AQChatConfig;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQRedisKeyPrefix;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.framework.giteeai.starter.DefaultGiteeAIClient;
import com.howcode.aqchat.framework.giteeai.starter.GiteeAIClient;
import com.howcode.aqchat.framework.giteeai.starter.config.GiteeAIConfiguration;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


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

    private List<UserGlobalInfoDto> aiList = new ArrayList<>();

    public List<UserGlobalInfoDto> getAiList() {
        return aiList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化小Q助手信息
        UserGlobalInfoDto aiInfo = new UserGlobalInfoDto();
        aiInfo.setUserId(AQBusinessConstant.AI_HELPER_ID);
        aiInfo.setUserName(AQBusinessConstant.AI_HELPER_NAME);
        aiInfo.setUserAvatar(AQBusinessConstant.AI_HELPER_AVATAR);
        aiList.add(aiInfo);
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_USER_INFO_PREFIX + AQBusinessConstant.AI_HELPER_ID, aiInfo);

        // 初始化小T信息
        UserGlobalInfoDto ttiInfo = new UserGlobalInfoDto();
        ttiInfo.setUserId(AQBusinessConstant.XT_ID);
        ttiInfo.setUserName(AQBusinessConstant.XT_NAME);
        ttiInfo.setUserAvatar(AQBusinessConstant.XT_AVATAR);
        aiList.add(ttiInfo);
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_USER_INFO_PREFIX + AQBusinessConstant.XT_ID, ttiInfo);

        // 初始化小V信息
        UserGlobalInfoDto ttvInfo = new UserGlobalInfoDto();
        ttvInfo.setUserId(AQBusinessConstant.XV_ID);
        ttvInfo.setUserName(AQBusinessConstant.XV_NAME);
        ttvInfo.setUserAvatar(AQBusinessConstant.XV_AVATAR);
        aiList.add(ttvInfo);
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_USER_INFO_PREFIX + AQBusinessConstant.XV_ID, ttvInfo);

        // 初始化小M信息
        UserGlobalInfoDto ttmInfo = new UserGlobalInfoDto();
        ttmInfo.setUserId(AQBusinessConstant.XM_ID);
        ttmInfo.setUserName(AQBusinessConstant.XM_NAME);
        ttmInfo.setUserAvatar(AQBusinessConstant.XM_AVATAR);
        aiList.add(ttmInfo);
        redisCacheHelper.setCacheObject(AQRedisKeyPrefix.AQ_USER_INFO_PREFIX + AQBusinessConstant.XM_ID, ttmInfo);

        LOGGER.info("AI助手信息初始化完成");
    }

    @Bean
    public GenerationParamHolder generationParamHolder() {
        String apiKey = aqChatConfig.getAiConfig().getBaiLianConfig().getApiKey();
        String model = aqChatConfig.getAiConfig().getBaiLianConfig().getModel();
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
    @Bean
    public GiteeAIConfiguration giteeAIConfiguration(){
        GiteeAIConfiguration giteeAIConfiguration = new GiteeAIConfiguration();
        giteeAIConfiguration.setBearer(aqChatConfig.getAiConfig().getGiteeConfig().getBearer());
        giteeAIConfiguration.setChatModel(aqChatConfig.getAiConfig().getGiteeConfig().getChatModel());
        giteeAIConfiguration.setTTIModel(aqChatConfig.getAiConfig().getGiteeConfig().getTtiModel());
        giteeAIConfiguration.setTTVModel(aqChatConfig.getAiConfig().getGiteeConfig().getTtvModel());
        return giteeAIConfiguration;
    }

    @Bean
    public GiteeAIClient giteeAIClient() {
        return new DefaultGiteeAIClient();
    }
}
