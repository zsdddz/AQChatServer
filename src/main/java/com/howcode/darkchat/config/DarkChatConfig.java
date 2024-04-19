package com.howcode.darkchat.config;

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
@EnableConfigurationProperties(DarkChatConfig.class)
@ConfigurationProperties(prefix = "dark-chat")
public class DarkChatConfig {
    private int webSocketPort;
    private int bossThreadSize;
    private int workThreadSize;
    private int heartBeatTime;
}
