package com.howcode.aqchat.service.receive;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.framework.mq.starter.config.RocketMQConfig;
import com.howcode.aqchat.service.service.IAQMessageService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 17:52
 */
@Component
public class StoreMessageReceiver implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreMessageReceiver.class);

    @Resource
    private RocketMQConfig rocketMQConfig;
    @Resource
    private IAQMessageService messageService;

    /**
     * 初始化消费者
     */
    public void initConsumer() {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getConsumer().getNameSever());
            defaultMQPushConsumer.setConsumerGroup(AQChatMQConstant.ConsumerGroup.STORE_MESSAGE_CONSUMER_GROUP);
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
            defaultMQPushConsumer.subscribe(AQChatMQConstant.MQTopic.STORE_MESSAGE_TOPIC, "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                for (MessageExt messageExt : messageExtList) {
                    String msgStr = new String(messageExt.getBody());
                    LOGGER.info("mq收到消息[存储消息]:{}", msgStr);
                    // 处理消息
                    if (!msgStr.isEmpty()){
                        MessageDto messageDto = JSONObject.parseObject(msgStr, MessageDto.class);
                        messageService.saveMessage(messageDto);
                    }
                }
                LOGGER.info("存储消息成功");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("[存储消息]订阅消息成功");
        } catch (MQClientException e) {
            LOGGER.error("订阅 存储消息 失败", e);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }
}
