package com.howcode.aqchat.framework.giteeai.starter.model.impl;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.framework.giteeai.starter.config.GiteeAIConfiguration;
import com.howcode.aqchat.framework.giteeai.starter.enums.RoleEnum;
import com.howcode.aqchat.framework.giteeai.starter.handler.MessageHandler;
import com.howcode.aqchat.framework.giteeai.starter.http.GiteeAIHttpDelegate;
import com.howcode.aqchat.framework.giteeai.starter.http.parameter.GeneralParameters;
import com.howcode.aqchat.framework.giteeai.starter.http.parameter.GeneralParametersBuilder;
import com.howcode.aqchat.framework.giteeai.starter.model.ChatModel;
import com.howcode.aqchat.framework.giteeai.starter.session.Message;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:59
 */
@Component
public class Llama3_70bChineseChat implements ChatModel {
    @Resource
    private GiteeAIConfiguration giteeAIConfiguration;
    @Override
    public String chat(String message) {
        return chat(message, null);
    }

    @Override
    public String chat(String message, List<Message> messages) {
        Message currMsg = new Message();
        currMsg.setContent(message);
        currMsg.setRole(RoleEnum.USER.getRole());
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(currMsg);
        GeneralParameters generalParameters = new GeneralParametersBuilder().messages(messages).stream(Boolean.FALSE).build();
        String data = null;
        try {
            data = GiteeAIHttpDelegate.sendPostRequestTT(giteeAIConfiguration.getChatModelUrl(), JSONObject.toJSONString(generalParameters), giteeAIConfiguration.getBearer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    @Override
    public void streamChat(String message, List<Message> messages, MessageHandler<String> handler) {
        Message currMsg = new Message();
        currMsg.setContent(message);
        currMsg.setRole(RoleEnum.USER.getRole());
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(currMsg);
        GeneralParameters generalParameters = new GeneralParametersBuilder().messages(messages).stream(Boolean.TRUE).build();
        try {
            GiteeAIHttpDelegate.sendPostAndReadStream(giteeAIConfiguration.getChatModelUrl(), JSONObject.toJSONString(generalParameters), giteeAIConfiguration.getBearer(), handler,null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
