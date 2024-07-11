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

        /**
         * 遗留数据的消费者组
         */
        String LEGACY_DATA_CONSUMER_GROUP = "legacy_data_consumer_group";
        /**
         * 存储消息的消费者组
         */
        String STORE_MESSAGE_CONSUMER_GROUP = "store_message_consumer_group";
        /**
         * 用户退出的消费者组
         */
        String USER_LOGOUT_CONSUMER_GROUP = "user_logout_consumer_group";


        String JOIN_ROOM_CONSUMER_GROUP = "join_room_consumer_group";
        String LEAVE_ROOM_CONSUMER_GROUP = "leave_room_consumer_group";
        String RECALL_MESSAGE_CONSUMER_GROUP = "recall_message_consumer_group";
        String AI_HELPER_CONSUMER_GROUP = "ai_helper_consumer_group";
        String XT_CONSUMER_GROUP = "xt_consumer_group";
        String XM_CONSUMER_GROUP = "xm_consumer_group";
        String XV_CONSUMER_GROUP = "xv_consumer_group";
        String Multiple_CONSUMER_GROUP = "multiple_consumer_group";
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
         * 存储消息的topic
         */
        String STORE_MESSAGE_TOPIC = "store_message_topic";

        /**
         * 成员离线消息的topic
         */
        String OFFLINE_MESSAGE_TOPIC = "offline_message_topic";
        /**
         * 遗留数据的topic
         */
        String LEGACY_DATA_TOPIC = "legacy_data_topic";
        /**
         * 用户退出的topic
         */
        String USER_LOGOUT_TOPIC = "user_logout_topic";
        /**
         * 离开房间的topic
         */
        String LEAVE_ROOM_TOPIC = "leave_room_topic";
        /**
         * 加入房间的topic
         */
        String JOIN_ROOM_TOPIC = "join_room_topic";
        /**
         * 撤回消息的topic
         */
        String RECALL_MESSAGE_TOPIC = "recall_message_topic";
        /**
         * 机器人助手的topic
         */
        String AI_HELPER_TOPIC = "ai_helper_topic";
        String XM_TOPIC = "xm_topic";
        String XT_TOPIC = "xt_topic";
        String XV_TOPIC = "xv_topic";
        String MULTIPLE_TOPIC = "multiple_topic";
    }
}
