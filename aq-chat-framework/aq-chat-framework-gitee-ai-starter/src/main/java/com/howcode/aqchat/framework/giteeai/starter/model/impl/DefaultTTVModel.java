package com.howcode.aqchat.framework.giteeai.starter.model.impl;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.framework.giteeai.starter.config.GiteeAIConfiguration;
import com.howcode.aqchat.framework.giteeai.starter.http.GiteeAIHttpDelegate;
import com.howcode.aqchat.framework.giteeai.starter.http.parameter.GeneralParameters;
import com.howcode.aqchat.framework.giteeai.starter.http.parameter.GeneralParametersBuilder;
import com.howcode.aqchat.framework.giteeai.starter.model.TTVModel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 12:00
 */
@Component
public class DefaultTTVModel implements TTVModel {
    @Resource
    private GiteeAIConfiguration giteeAIConfiguration;
    @Override
    public byte[] textToVoice(String message) {
        GeneralParameters generalParameters = new GeneralParametersBuilder().inputs(message).build();
        try {
            return GiteeAIHttpDelegate.sendPostRequestTB(giteeAIConfiguration.getTtvModelUrl(), JSONObject.toJSONString(generalParameters), giteeAIConfiguration.getBearer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
