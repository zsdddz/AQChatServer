package com.howcode.aqchat.mq.receive;

import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.framework.mq.starter.config.RocketMQConfig;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IRoomHolder;
import com.howcode.aqchat.holder.IUserHolder;
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
 * @date 2024-04-27 1:06
 */
@Component
public class LegacyDataReceiver implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyDataReceiver.class);
    @Resource
    private RocketMQConfig rocketMQConfig;
    @Resource
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    private IUserHolder userHolder;
    @Resource
    private IRoomHolder roomHolder;

    /**
     * 初始化消费者
     */
    public void initConsumer() {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConfig.getConsumer().getNameSever());
            defaultMQPushConsumer.setConsumerGroup(AQChatMQConstant.ConsumerGroup.LEGACY_DATA_CONSUMER_GROUP);
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(AQChatMQConstant.MQTopic.LEGACY_DATA_TOPIC, "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                for (MessageExt messageExt : messageExtList) {
                    String userId = new String(messageExt.getBody());
                    LOGGER.info("mq收到消息[离线遗留]:{}", userId);
                    // 广播用户退出消息
                    if (!userId.isEmpty()) {
                        try {
                            UserGlobalInfoDto userInfo = userHolder.getUserInfo(userId);
                            if (userInfo == null){
                                //清理遗留信息
                                LOGGER.info("用户离线时间超过系统限制，开始清理遗留信息");
                                String roomId = globalChannelHolder.getRoomId(userId);
                                roomHolder.removeRoomMember(roomId,userId);
                                globalChannelHolder.removeByBroadcaster(userId);
                                globalChannelHolder.dissolveTheRoomByLogout(roomId);
                            }
                        }catch (Exception e){
                            LOGGER.error("[离线遗留] MQ 处理异常",e);
                        }
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("[离线遗留]订阅消息成功");
        } catch (MQClientException e) {
            LOGGER.error("离线遗留 订阅失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }
}
