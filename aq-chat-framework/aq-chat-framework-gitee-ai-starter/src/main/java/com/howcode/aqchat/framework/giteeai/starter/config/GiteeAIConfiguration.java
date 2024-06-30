package com.howcode.aqchat.framework.giteeai.starter.config;


import com.howcode.aqchat.framework.giteeai.starter.constants.AIModel;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:41
 */
@Configuration
public class GiteeAIConfiguration {
    private String bearer;
    private String ChatModel;
    private String chatModelUrl;
    private String TTIModel;
    private String tTIModelUrl;
    private String TTVModel;
    private String tTVModelUrl;

    @Override
    public String toString() {
        return "GiteeAIConfiguration{" +
                "bearer='" + bearer + '\'' +
                ", ChatModel='" + ChatModel + '\'' +
                ", chatModelUrl='" + chatModelUrl + '\'' +
                ", TTIModel='" + TTIModel + '\'' +
                ", tTIModelUrl='" + tTIModelUrl + '\'' +
                ", TTVModel='" + TTVModel + '\'' +
                ", tTVModelUrl='" + tTVModelUrl + '\'' +
                '}';
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getChatModel() {
        return ChatModel;
    }

    public void setChatModel(String chatModel) {
        ChatModel = chatModel;
        setChatModelUrl(AIModel.AI_MODEL_MAP.get(chatModel));
    }

    public String getChatModelUrl() {
        return chatModelUrl;
    }

    private void setChatModelUrl(String chatModelUrl) {
        this.chatModelUrl = chatModelUrl;
    }

    public String getTTIModel() {
        return TTIModel;
    }

    public void setTTIModel(String TTIModel) {
        this.TTIModel = TTIModel;
        settTIModelUrl(AIModel.AI_MODEL_MAP.get(TTIModel));
    }

    public String gettTIModelUrl() {
        return tTIModelUrl;
    }

    private void settTIModelUrl(String tTIModelUrl) {
        this.tTIModelUrl = tTIModelUrl;
    }

    public String getTTVModel() {
        return TTVModel;
    }

    public void setTTVModel(String TTVModel) {
        this.TTVModel = TTVModel;
        settTVModelUrl(AIModel.AI_MODEL_MAP.get(TTVModel));
    }

    public String gettTVModelUrl() {
        return tTVModelUrl;
    }

    private void settTVModelUrl(String tTVModelUrl) {
        this.tTVModelUrl = tTVModelUrl;
    }
}
