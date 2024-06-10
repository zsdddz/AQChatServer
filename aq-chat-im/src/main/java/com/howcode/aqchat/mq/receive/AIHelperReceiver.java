package com.howcode.aqchat.mq.receive;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.ai.IAiService;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.model.AIMessageDto;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.model.RoomNotifyDto;
import com.howcode.aqchat.framework.mq.starter.config.RocketMQConfig;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IRoomHolder;
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
 * @date 2024-06-10 18:27
 */
@Component
public class AIHelperReceiver implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinRoomReceiver.class);

    @Resource
    private RocketMQConfig rocketMQConfig;
    @Resource
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    private IAiService aiService;
    /**
     * 初始化消费者
     */
    public void initConsumer() {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getConsumer().getNameSever());
            defaultMQPushConsumer.setConsumerGroup(AQChatMQConstant.ConsumerGroup.AI_HELPER_CONSUMER_GROUP);
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(AQChatMQConstant.MQTopic.AI_HELPER_TOPIC, "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                for (MessageExt messageExt : messageExtList) {
                    String msgStr = new String(messageExt.getBody());
                    LOGGER.info("mq收到消息[AI助手]:{}", msgStr);
                    // AI助手处理消息
                    if (!msgStr.isEmpty()){
                        MessageDto messageDto = JSONObject.parseObject(msgStr, MessageDto.class);
                        aiService.streamCallWithMessage(messageDto.getMessageContent(),aiResult -> {
                            AIMessageDto aiMessageDto = new AIMessageDto();
                            aiMessageDto.setRoomId(messageDto.getRoomId());
                            aiMessageDto.setContent(aiResult.getContent());
                            aiMessageDto.setStatus(aiResult.getStatus());
                            globalChannelHolder.sendBroadcastAIMessage(aiMessageDto);
                        });
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("[AI助手]订阅消息成功");
        } catch (MQClientException e) {
            LOGGER.error("AI助手 订阅失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }
}
