package com.howcode.aqchat.framework.giteeai.starter.config;


import com.howcode.aqchat.framework.giteeai.starter.constants.AIModel;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:41
 */
public class GiteeAIConfiguration {
    private final String bearer;
    private final String chatModel;
    private final String chatModelUrl;
    private final String ttiModel;
    private final String ttiModelUrl;
    private final String ttvModel;
    private final String ttvModelUrl;

    public String getBearer() {
        return bearer;
    }

    public String getChatModel() {
        return chatModel;
    }


    public String getChatModelUrl() {
        return chatModelUrl;
    }

    public String getTtiModel() {
        return ttiModel;
    }

    public String getTtiModelUrl() {
        return ttiModelUrl;
    }

    public String getTtvModel() {
        return ttvModel;
    }

    public String getTtvModelUrl() {
        return ttvModelUrl;
    }

    private GiteeAIConfiguration(Builder builder) {
        this.bearer = builder.bearer;
        this.chatModel = builder.chatModel;
        this.chatModelUrl = builder.chatModelUrl;
        this.ttiModel = builder.ttiModel;
        this.ttiModelUrl = builder.ttiModelUrl;
        this.ttvModel = builder.ttvModel;
        this.ttvModelUrl = builder.ttvModelUrl;
    }

    public static class Builder {
        private String bearer;
        private String chatModel;
        private String chatModelCode;
        private String chatModelUrl;
        private String ttiModel;
        private String ttiModelCode;
        private String ttiModelUrl;
        private String ttvModel;
        private String ttvModelCode;
        private String ttvModelUrl;

        public Builder setBearer(String bearer) {
            this.bearer = bearer;
            return this;
        }

        public Builder setChatModel(String chatModel) {
            this.chatModel = chatModel;
            return this;
        }

        public Builder setChatModelCode(String chatModelCode) {
            this.chatModelCode = chatModelCode;
            return this;
        }

        public Builder setTtiModel(String ttiModel) {
            this.ttiModel = ttiModel;
            return this;
        }

        public Builder setTtiModelCode(String ttiModelCode) {
            this.ttiModelCode = ttiModelCode;
            return this;
        }


        public Builder setTtvModel(String ttvModel) {
            this.ttvModel = ttvModel;
            return this;
        }

        public Builder setTtvModelCode(String ttvModelCode) {
            this.ttvModelCode = ttvModelCode;
            return this;
        }


        public GiteeAIConfiguration build() {
            String chatModelUrl = AIModel.AI_MODEL_MAP.get(this.chatModel).replace(AIModel.CHAT_MODEL_CODE, this.chatModelCode);
            String ttiModelUrl = AIModel.AI_MODEL_MAP.get(this.ttiModel).replace(AIModel.TTI_MODEL_CODE, this.ttiModelCode);
            String ttvModelUrl = AIModel.AI_MODEL_MAP.get(this.ttvModel).replace(AIModel.TTV_MODEL_CODE, this.ttvModelCode);
            this.chatModelUrl = chatModelUrl;
            this.ttiModelUrl = ttiModelUrl;
            this.ttvModelUrl = ttvModelUrl;
            return new GiteeAIConfiguration(this);
        }
    }

    @Override
    public String toString() {
        return "GiteeAIConfiguration{" +
                "bearer='" + bearer + '\'' +
                ", chatModel='" + this.chatModel + '\'' +
                ", chatModelUrl='" + chatModelUrl + '\'' +
                ", ttiModel='" + ttiModel + '\'' +
                ", ttiModelUrl='" + ttiModelUrl + '\'' +
                ", ttvModel='" + ttvModel + '\'' +
                ", ttvModelUrl='" + ttvModelUrl + '\'' +
                '}';
    }
}
