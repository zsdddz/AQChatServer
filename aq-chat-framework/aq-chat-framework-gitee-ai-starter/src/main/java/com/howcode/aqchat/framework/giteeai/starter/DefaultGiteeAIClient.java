package com.howcode.aqchat.framework.giteeai.starter;

import com.howcode.aqchat.framework.giteeai.starter.config.GiteeAIConfiguration;
import com.howcode.aqchat.framework.giteeai.starter.factory.ModelFactory;
import com.howcode.aqchat.framework.giteeai.starter.handler.MessageHandler;
import com.howcode.aqchat.framework.giteeai.starter.session.Message;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 12:02
 */
@Component
@ConditionalOnBean(GiteeAIConfiguration.class)
public class DefaultGiteeAIClient implements GiteeAIClient{

    @Resource
    private ModelFactory modelFactory;

    @Override
    public String chat(String message) {
        return modelFactory.getChatModel().chat(message);
    }

    @Override
    public String chat(String message, List<Message> messages) {
        return modelFactory.getChatModel().chat(message, messages);
    }

    @Override
    public void streamChat(String message, List<Message> messages,  MessageHandler<String> handler) {
        modelFactory.getChatModel().streamChat(message, messages, handler);
    }

    @Override
    public byte[] textToImage(String message) {
        return modelFactory.getTTIModel().textToImage(message);
    }

    @Override
    public byte[] textToVoice(String message) {
        return modelFactory.getTTVModel().textToVoice(message);
    }

}
