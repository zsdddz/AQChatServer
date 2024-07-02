package com.howcode.aqchat.framework.giteeai.starter.factory;

import com.howcode.aqchat.framework.giteeai.starter.config.GiteeAIConfiguration;
import com.howcode.aqchat.framework.giteeai.starter.constants.AIModel;
import com.howcode.aqchat.framework.giteeai.starter.model.ChatModel;
import com.howcode.aqchat.framework.giteeai.starter.model.TTIModel;
import com.howcode.aqchat.framework.giteeai.starter.model.TTVModel;
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


    public ChatModel getChatModel() {
        return applicationContext.getBean(ChatModel.class);
    }
    public TTIModel getTTIModel() {
        return  applicationContext.getBean(TTIModel.class);
    }
    public TTVModel getTTVModel() {
        return applicationContext.getBean(TTVModel.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
