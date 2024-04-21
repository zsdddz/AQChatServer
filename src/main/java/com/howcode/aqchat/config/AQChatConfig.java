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
@EnableConfigurationProperties(AQChatConfig.class)
@ConfigurationProperties(prefix = "aq-chat")
public class AQChatConfig {
    private int webSocketPort;
    private int bossThreadSize;
    private int workThreadSize;
    private int heartBeatTime;
}
