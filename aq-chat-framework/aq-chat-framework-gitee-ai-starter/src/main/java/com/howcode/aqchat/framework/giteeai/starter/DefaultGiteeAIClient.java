package com.howcode.aqchat.framework.giteeai.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.framework.giteeai.starter.config.GiteeAIConfiguration;
import com.howcode.aqchat.framework.giteeai.starter.constants.AIModel;
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
public class DefaultGiteeAIClient implements GiteeAIClient {

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
    public void streamChat(String message, List<Message> messages, MessageHandler<String> handler) {
        modelFactory.getChatModel().streamChat(message, messages, handler);
    }

    @Override
    public byte[] textToImage(String message) {
        String finalContext = chat(AIModel.AI_TRANSLATE_GUIDE_WORD + message);
        String contentFromJson = getContentFromJson(finalContext);
        if (contentFromJson == null) {
            contentFromJson = message;
        }
        return modelFactory.getTTIModel().textToImage(contentFromJson);
    }

    @Override
    public byte[] textToVoice(String message) {
        return modelFactory.getTTVModel().textToVoice(message);
    }

    private static String getContentFromJson(String jsonString) {
        // 解析JSON字符串
        JSONObject jsonObject = JSON.parseObject(jsonString);
        // 获取choices数组
        JSONArray choices = jsonObject.getJSONArray("choices");
        // 获取第一个对象中的message对象
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        // 获取content字段
        return message.getString("content");
    }
}
