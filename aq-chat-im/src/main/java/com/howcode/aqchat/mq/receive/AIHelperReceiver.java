package com.howcode.aqchat.mq.receive;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.ai.IAiService;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.enums.AIMessageStatusEnum;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.enums.MessageStatusEnum;
import com.howcode.aqchat.common.enums.MsgTypeEnum;
import com.howcode.aqchat.common.model.AIMessageDto;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.model.RoomNotifyDto;
import com.howcode.aqchat.common.utils.ThreadPoolUtil;
import com.howcode.aqchat.framework.mq.starter.config.RocketMQConfig;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IRoomHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.service.service.IAQMessageService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    @Resource
    private IAQMessageService messageService;
    @Resource
    private ThreadPoolUtil threadPoolUtil;

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
                    threadPoolUtil.submitTask(()->{
                        // AI助手处理消息
                        if (!msgStr.isEmpty()) {
                            MessageDto messageDto = JSONObject.parseObject(msgStr, MessageDto.class);
                            StringBuilder fullContent = new StringBuilder();
                            try {
                                aiService.streamCallWithMessage(messageDto.getMessageContent(), aiResult -> {
                                    AIMessageDto aiMessageDto = new AIMessageDto();
                                    aiMessageDto.setMessageId(messageDto.getMessageId());
                                    aiMessageDto.setRoomId(messageDto.getRoomId());
                                    aiMessageDto.setContent(aiResult.getContent());
                                    aiMessageDto.setStatus(aiResult.getStatus());
                                    globalChannelHolder.sendBroadcastAIMessage(aiMessageDto);
                                    fullContent.append(aiResult.getContent());
                                });
                            } catch (Exception e) {
                                LOGGER.error("AI助手处理消息失败", e);
                                //发送失败消息
                                AIMessageDto aiMessageDto = new AIMessageDto();
                                aiMessageDto.setMessageId(messageDto.getMessageId());
                                aiMessageDto.setRoomId(messageDto.getRoomId());
                                aiMessageDto.setStatus(AIMessageStatusEnum.FAIL.getCode());
                                globalChannelHolder.sendBroadcastAIMessage(aiMessageDto);
                            } finally {
                                LOGGER.info("开始存储AI回复消息");
                                MessageDto storeMessage = buildStoreMessage(messageDto, fullContent);
                                messageService.saveMessage(storeMessage);
                            }
                        }
                    });
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("[AI助手]订阅消息成功");
        } catch (MQClientException e) {
            LOGGER.error("AI助手 订阅失败", e);
        }
    }

    private static MessageDto buildStoreMessage(MessageDto messageDto, StringBuilder fullContent) {
        MessageDto storeMessage = new MessageDto();
        storeMessage.setMessageId(messageDto.getMessageId());
        storeMessage.setMessageType(MsgTypeEnum.TEXT.getCode());
        storeMessage.setSenderId(AQBusinessConstant.AI_HELPER_ID);
        storeMessage.setRoomId(messageDto.getRoomId());
        storeMessage.setCreateTime(new Date());
        storeMessage.setMessageExt(AQBusinessConstant.AT + messageDto.getSenderId());
        storeMessage.setMessageContent(fullContent.toString());
        return storeMessage;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }
}
