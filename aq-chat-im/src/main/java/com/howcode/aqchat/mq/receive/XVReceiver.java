package com.howcode.aqchat.mq.receive;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.ai.AIResult;
import com.howcode.aqchat.ai.IAiService;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.enums.AIMessageStatusEnum;
import com.howcode.aqchat.common.enums.MsgTypeEnum;
import com.howcode.aqchat.common.model.AIMessageDto;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.utils.ThreadPoolUtil;
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

import java.util.Date;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 16:55
 */
@Component
public class XVReceiver implements InitializingBean {

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

    /**
     * 初始化消费者
     */
    public void initConsumer() {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getConsumer().getNameSever());
            defaultMQPushConsumer.setConsumerGroup(AQChatMQConstant.ConsumerGroup.XV_CONSUMER_GROUP);
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(AQChatMQConstant.MQTopic.XV_TOPIC, "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                for (MessageExt messageExt : messageExtList) {
                    String msgStr = new String(messageExt.getBody());
                    LOGGER.info("mq收到消息[XM 文本转语音模型]:{}", msgStr);
                    threadPoolUtil.submitTask(() -> {
                        // AI助手处理消息
                        if (!msgStr.isEmpty()) {
                            MessageDto messageDto = JSONObject.parseObject(msgStr, MessageDto.class);
                            StringBuilder fullContent = new StringBuilder();
                            try {
                                AIResult aiResult = aiService.textToVoice(messageDto.getMessageContent());
                                AIMessageDto aiMessageDto = new AIMessageDto();
                                aiMessageDto.setMessageId(messageDto.getMessageId());
                                aiMessageDto.setRoomId(messageDto.getRoomId());
                                aiMessageDto.setContent(aiResult.getContent());
                                aiMessageDto.setStatus(aiResult.getStatus());
                                aiMessageDto.setMsgType(MsgTypeEnum.VOICE.getCode());
                                fullContent.append(aiResult.getContent());

                                globalChannelHolder.sendAIMessage(aiMessageDto, AQBusinessConstant.XV_ID, MsgTypeEnum.VOICE.getCode());
                            } catch (Exception e) {
                                LOGGER.error(" 文本转语音模型 处理消息失败", e);
                                //发送失败消息
                                AIMessageDto aiMessageDto = new AIMessageDto();
                                aiMessageDto.setMessageId(messageDto.getMessageId());
                                aiMessageDto.setRoomId(messageDto.getRoomId());
                                aiMessageDto.setStatus(AIMessageStatusEnum.FAIL.getCode());
                                globalChannelHolder.sendAIMessage(aiMessageDto, AQBusinessConstant.XV_ID, MsgTypeEnum.VOICE.getCode());
                            } finally {
                                LOGGER.info("开始存储 文本转语音模型 AI 回复消息");
                                MessageDto storeMessage = buildStoreMessage(messageDto, fullContent);
                                messageService.saveMessage(storeMessage);
                                LOGGER.info("文本转语音模型 AI 回复消息存储成功");
                            }
                        }
                    });
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("[文本转语音模型]订阅消息成功");
        } catch (MQClientException e) {
            LOGGER.error("文本转语音模型 订阅失败", e);
        }
    }

    private static MessageDto buildStoreMessage(MessageDto messageDto, StringBuilder fullContent) {
        MessageDto storeMessage = new MessageDto();
        storeMessage.setMessageId(messageDto.getMessageId());
        storeMessage.setMessageType(MsgTypeEnum.VOICE.getCode());
        storeMessage.setSenderId(AQBusinessConstant.XV_ID);
        storeMessage.setRoomId(messageDto.getRoomId());
        storeMessage.setCreateTime(new Date());
        int lastIndex = fullContent.toString().lastIndexOf('/');
        if (lastIndex != -1) {
            String fileName = fullContent.substring(lastIndex + 1);
            storeMessage.setMessageExt(fileName);
        }
        storeMessage.setMessageContent(fullContent.toString());
        return storeMessage;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }
}
