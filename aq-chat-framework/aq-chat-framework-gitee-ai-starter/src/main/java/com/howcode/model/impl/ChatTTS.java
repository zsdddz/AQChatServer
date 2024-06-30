package com.howcode.model.impl;

import com.alibaba.fastjson.JSONObject;
import com.howcode.config.GiteeAIConfiguration;
import com.howcode.http.GiteeAIHttpDelegate;
import com.howcode.http.parameter.GeneralParameters;
import com.howcode.http.parameter.GeneralParametersBuilder;
import com.howcode.model.TTVModel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 12:00
 */
@Component
public class ChatTTS implements TTVModel {
    @Resource
    private GiteeAIConfiguration giteeAIConfiguration;
    @Override
    public byte[] textToVoice(String message) {
        GeneralParameters generalParameters = new GeneralParametersBuilder().inputs(message).build();
        try {
            return GiteeAIHttpDelegate.sendPostRequestTB(giteeAIConfiguration.gettTVModelUrl(), JSONObject.toJSONString(generalParameters), giteeAIConfiguration.getBearer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
