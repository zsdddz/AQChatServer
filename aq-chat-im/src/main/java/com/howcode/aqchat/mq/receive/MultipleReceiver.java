package com.howcode.aqchat.mq.receive;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.ai.IAiService;
import com.howcode.aqchat.ai.parameter.MessageRecord;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.enums.AIMessageStatusEnum;
import com.howcode.aqchat.common.enums.MsgTypeEnum;
import com.howcode.aqchat.common.model.AIMessageDto;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.utils.ThreadPoolUtil;
import com.howcode.aqchat.framework.giteeai.starter.enums.RoleEnum;
import com.howcode.aqchat.framework.mq.starter.config.RocketMQConfig;
import com.howcode.aqchat.framework.redis.starter.RedisCacheHelper;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IRoomHolder;
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

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 17:20
 */
@Component
public class MultipleReceiver implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinRoomReceiver.class);

    @Resource
    private RocketMQConfig rocketMQConfig;
    @Resource
    private GlobalChannelHolder globalChannelHolder;
    @Resource(name = "giteeAIService")
    private IAiService aiService;
    @Resource
    private IAQMessageService messageService;
    @Resource
    private ThreadPoolUtil threadPoolUtil;
    @Resource
    private IRoomHolder roomHolder;

    /**
     * 初始化消费者
     */
    public void initConsumer() {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getConsumer().getNameSever());
            defaultMQPushConsumer.setConsumerGroup(AQChatMQConstant.ConsumerGroup.Multiple_CONSUMER_GROUP);
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(AQChatMQConstant.MQTopic.MULTIPLE_TOPIC, "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                for (MessageExt messageExt : messageExtList) {
                    String msgStr = new String(messageExt.getBody());
                    LOGGER.info("mq收到消息[XT 多轮对话]:{}", msgStr);
                    threadPoolUtil.submitTask(() -> {
                        // AI助手处理消息
                        if (!msgStr.isEmpty()) {
                            MessageDto messageDto = JSONObject.parseObject(msgStr, MessageDto.class);
                            StringBuilder fullContent = new StringBuilder();
                            try {
                                List<MessageRecord> roomConversationRecords = roomHolder.getRoomConversationRecords(messageDto.getRoomId());
                                aiService.chat(messageDto.getMessageContent(),roomConversationRecords, aiResult -> {
                                    AIMessageDto aiMessageDto = new AIMessageDto();
                                    aiMessageDto.setMessageId(messageDto.getMessageId());
                                    aiMessageDto.setRoomId(messageDto.getRoomId());
                                    aiMessageDto.setContent(aiResult.getContent());
                                    aiMessageDto.setStatus(aiResult.getStatus());
                                    globalChannelHolder.sendBroadcastAIMessage(aiMessageDto, AQBusinessConstant.XT_ID);
                                    fullContent.append(aiResult.getContent());
                                });
                            } catch (Exception e) {
                                LOGGER.error(" 多轮对话 处理消息失败", e);
                                //发送失败消息
                                AIMessageDto aiMessageDto = new AIMessageDto();
                                aiMessageDto.setMessageId(messageDto.getMessageId());
                                aiMessageDto.setRoomId(messageDto.getRoomId());
                                aiMessageDto.setStatus(AIMessageStatusEnum.FAIL.getCode());
                                globalChannelHolder.sendBroadcastAIMessage(aiMessageDto, AQBusinessConstant.XT_ID);
                            } finally {
                                LOGGER.info("开始存储 多轮对话 回复消息");
                                MessageDto storeMessage = buildStoreMessage(messageDto, fullContent);
                                //添加对话记录到房间
                                MessageRecord userRecord = new MessageRecord();
                                userRecord.setRole(RoleEnum.USER.getRole());
                                userRecord.setContent(messageDto.getMessageContent());
                                roomHolder.addRoomConversationRecord(messageDto.getRoomId(),userRecord);

                                MessageRecord systemRecord = new MessageRecord();
                                systemRecord.setRole(RoleEnum.SYSTEM.getRole());
                                systemRecord.setContent(fullContent.toString());
                                roomHolder.addRoomConversationRecord(messageDto.getRoomId(),systemRecord);

                                messageService.saveMessage(storeMessage);
                                LOGGER.info("多轮对话 回复消息存储成功");
                            }
                        }
                    });
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("[多轮对话]订阅消息成功");
        } catch (MQClientException e) {
            LOGGER.error("多轮对话 订阅失败", e);
        }
    }

    private static MessageDto buildStoreMessage(MessageDto messageDto, StringBuilder fullContent) {
        MessageDto storeMessage = new MessageDto();
        storeMessage.setMessageId(messageDto.getMessageId());
        storeMessage.setMessageType(MsgTypeEnum.TEXT.getCode());
        storeMessage.setSenderId(AQBusinessConstant.XT_ID);
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