package com.howcode.aqchat.framework.giteeai.starter.model.impl;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.framework.giteeai.starter.config.GiteeAIConfiguration;
import com.howcode.aqchat.framework.giteeai.starter.http.GiteeAIHttpDelegate;
import com.howcode.aqchat.framework.giteeai.starter.http.parameter.GeneralParameters;
import com.howcode.aqchat.framework.giteeai.starter.http.parameter.GeneralParametersBuilder;
import com.howcode.aqchat.framework.giteeai.starter.model.TTIModel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:59
 */
@Component
public class StableDiffusion_3Medium implements TTIModel {
    @Resource
    private GiteeAIConfiguration giteeAIConfiguration;
    @Override
    public byte[] textToImage(String message) {
        GeneralParameters generalParameters = new GeneralParametersBuilder().inputs(message).build();
        try {
            return GiteeAIHttpDelegate.sendPostRequestTB(giteeAIConfiguration.gettTIModelUrl(), JSONObject.toJSONString(generalParameters), giteeAIConfiguration.getBearer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
