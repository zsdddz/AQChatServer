package com.howcode.aqchat.common.constant;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 17:46
 */
public class AQChatMQConstant {

    /**
     * 消费者组
     */
    public interface ConsumerGroup {
        /**
         * 发送消息的消费者组
         */
        String SEND_MESSAGE_CONSUMER_GROUP = "send_message_consumer_group";

        /**
         * 离线消息的消费者组
         */
        String OFFLINE_MESSAGE_CONSUMER_GROUP = "offline_message_consumer_group";
    }
    /**
     * MQ的topic
     */
    public interface MQTopic {
        /**
         * 发送消息的topic
         */
        String SEND_MESSAGE_TOPIC = "send_message_topic";

        /**
         * 成员离线消息的topic
         */
        String OFFLINE_MESSAGE_TOPIC = "offline_message_topic";
    }
}
