package com.howcode.aqchat.framework.mq.starter.producer;

import com.howcode.aqchat.framework.mq.starter.config.RocketMQConfig;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 16:34
 */
@Configuration
public class RocketMQProducerConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;
    @Resource
    private RocketMQConfig rocketMQConfig;

    @Bean
    public MQProducer mqProducer() {
        ThreadPoolExecutor asyncThreadPoolExecutor = new ThreadPoolExecutor(100, 150, 3, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), r -> {
            Thread thread = new Thread(r);
            thread.setName(applicationName + ":rocketmq-produce-" + ThreadLocalRandom.current().nextInt(1000));
            return thread;
        });
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        try {
            defaultMQProducer.setNamesrvAddr(rocketMQConfig.getNameSever());
            defaultMQProducer.setProducerGroup(rocketMQConfig.getGroupName());
            defaultMQProducer.setRetryTimesWhenSendFailed(rocketMQConfig.getRetryTimes());
            defaultMQProducer.setRetryTimesWhenSendAsyncFailed(rocketMQConfig.getRetryTimes());
            defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
            //设置发送信息的异步线程池
            defaultMQProducer.setAsyncSenderExecutor(asyncThreadPoolExecutor);
            defaultMQProducer.start();
            System.out.println("RocketMQ 生产者启动成功====>NameSever is "+ rocketMQConfig.getNameSever());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        return defaultMQProducer;
    }
}
