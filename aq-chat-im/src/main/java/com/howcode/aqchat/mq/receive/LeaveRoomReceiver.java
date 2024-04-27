package com.howcode.aqchat.mq.receive;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.model.RoomNotifyDto;
import com.howcode.aqchat.framework.mq.starter.config.RocketMQConfig;
import com.howcode.aqchat.holder.GlobalChannelHolder;
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
 * @date 2024-04-27 11:59
 */
@Component
public class LeaveRoomReceiver implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveRoomReceiver.class);
    @Resource
    private RocketMQConfig rocketMQConfig;
    @Resource
    private GlobalChannelHolder globalChannelHolder;

    /**
     * 初始化消费者
     */
    public void initConsumer() {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getConsumer().getNameSever());
            defaultMQPushConsumer.setConsumerGroup(AQChatMQConstant.ConsumerGroup.LEAVE_ROOM_CONSUMER_GROUP);
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(AQChatMQConstant.MQTopic.LEAVE_ROOM_TOPIC, "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                for (MessageExt messageExt : messageExtList) {
                    String msgStr = new String(messageExt.getBody());
                    LOGGER.info("mq收到消息[离开房间]:{}", msgStr);
                    // 广播用户加入房间
                    if (!msgStr.isEmpty()) {
                        RoomNotifyDto roomNotifyDto = JSONObject.parseObject(msgStr, RoomNotifyDto.class);
                        globalChannelHolder.notifyLeaveRoom(roomNotifyDto);
                    }

                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("[离开房间]订阅消息成功");
        } catch (MQClientException e) {
            LOGGER.error("离开房间 订阅失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }
}
