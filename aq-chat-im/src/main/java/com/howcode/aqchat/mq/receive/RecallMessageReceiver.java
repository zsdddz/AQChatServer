package com.howcode.aqchat.mq.receive;

import com.alibaba.fastjson.JSON;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.model.RecallMessageDto;
import com.howcode.aqchat.framework.mq.starter.config.RocketMQConfig;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.service.service.IAQMessageService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-26 18:53
 */
@Component
public class RecallMessageReceiver implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecallMessageReceiver.class);
    @Resource
    private RocketMQConfig rocketMQConfig;
    @Resource
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    private IAQMessageService messageService;
    /**
     * 初始化消费者
     */
    public void initConsumer() {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getConsumer().getNameSever());
            defaultMQPushConsumer.setConsumerGroup(AQChatMQConstant.ConsumerGroup.RECALL_MESSAGE_CONSUMER_GROUP);
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(AQChatMQConstant.MQTopic.RECALL_MESSAGE_TOPIC, "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                for (MessageExt messageExt : messageExtList) {
                    String optJson = new String(messageExt.getBody());
                    LOGGER.info("mq收到消息[撤回消息]:{}", optJson);
                    // 广播用户撤回消息
                    if (!optJson.isEmpty()) {
                        try {
                            RecallMessageDto recallMessageDto = JSON.parseObject(optJson,RecallMessageDto.class);
                            globalChannelHolder.notifyRecallMessage(recallMessageDto);
                            //设置不可见
                            messageService.updateMessageVisible(recallMessageDto.getMsgId());
                        }catch (Exception e){
                            LOGGER.error("撤回消息MQ异常",e);
                        }
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("[撤回消息]订阅消息成功");
        } catch (MQClientException e) {
            LOGGER.error("订阅撤回消息失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }
}
