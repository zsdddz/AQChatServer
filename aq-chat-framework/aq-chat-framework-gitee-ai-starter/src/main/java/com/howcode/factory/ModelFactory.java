package com.howcode.factory;

import com.howcode.config.GiteeAIConfiguration;
import com.howcode.constants.AIModel;
import com.howcode.model.ChatModel;
import com.howcode.model.TTIModel;
import com.howcode.model.TTVModel;
import jakarta.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 13:07
 */
@Component
public class ModelFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Resource
    private GiteeAIConfiguration giteeAIConfiguration;

    private static final Map<String,String> modelMap = Map.of(
            AIModel.LLAMA3_70B_CHINESE_CHAT, "llama3_70bChineseChat",
            AIModel.CHAT_TTS, "chatTTS",
            AIModel.STABLE_DIFFUSION_3_MEDIUM, "stableDiffusion_3Medium"
    );

    public ChatModel getChatModel() {
        return applicationContext.getBean(modelMap.get(giteeAIConfiguration.getChatModel()), ChatModel.class);
    }
    public TTIModel getTTIModel() {
        return  applicationContext.getBean(modelMap.get(giteeAIConfiguration.getTTIModel()), TTIModel.class);
    }
    public TTVModel getTTVModel() {
        return applicationContext.getBean(modelMap.get(giteeAIConfiguration.getTTVModel()), TTVModel.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
