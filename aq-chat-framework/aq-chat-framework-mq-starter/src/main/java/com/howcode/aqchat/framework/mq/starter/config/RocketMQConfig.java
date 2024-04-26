package com.howcode.aqchat.framework.mq.starter.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 16:29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aq-chat.mq.rocketmq")
public class RocketMQConfig {
    /**
     * rocketmq的nameServer地址
     */
    private String nameSever;
    /**
     * 分组名字
     */
    private String groupName;
    /**
     * 信息重发次数
     */
    private int retryTimes;
    /**
     * 超时时间
     */
    private int sendTimeOut;
}
