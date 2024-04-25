package com.howcode.aqchat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-19 19:22
 */
@Data
@Component
@ConfigurationProperties(prefix = "aq-chat")
public class AQChatConfig {
    private int webSocketPort;
    private int bossThreadSize;
    private int workThreadSize;
    private Long heartBeatTime;
    private AliOssConfig aliOssConfig;
    private AliOssStsConfig aliOssStsConfig;

    @Data
    public static class AliOssConfig {
        private String endpoint;
    }

    @Data
    public static class AliOssStsConfig {
        private String accessKeyId;
        private String accessKeySecret;
        private String bucketName;
        private String endpoint;
        private String regionId;
        private String roleSessionName;
        private String roleArn;
        private Long durationSeconds;
    }
}
