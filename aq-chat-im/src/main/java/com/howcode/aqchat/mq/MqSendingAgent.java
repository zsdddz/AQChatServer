package com.howcode.aqchat.mq;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.model.RecallMessageDto;
import com.howcode.aqchat.common.model.RoomNotifyDto;
import com.howcode.aqchat.common.model.MessageDto;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 20:53
 */
@Component
public class MqSendingAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqSendingAgent.class);

    @Resource
    private MQProducer mqProducer;

    public void sendOfflineMessage(String userId) {
        if (null == userId) {
            LOGGER.error("离线消息用户id为空");
            return;
        }
        //发送离线消息
        Message offlineMessage = new Message();
        offlineMessage.setBody(userId.getBytes());
        offlineMessage.setTopic(AQChatMQConstant.MQTopic.OFFLINE_MESSAGE_TOPIC);

        //发送遗留数据处理通知
        Message legacyDataMessage = new Message();
        legacyDataMessage.setBody(userId.getBytes());
        legacyDataMessage.setTopic(AQChatMQConstant.MQTopic.LEGACY_DATA_TOPIC);
        // 1  2  3    4    5  6  7   8   9  10  11 12 13  14  15  16  17  18
        // 1s 5s 10s  30s  1m 2m 3m  4m 5m  6m  7m 8m 9m 10m  20m  30m 1h  2h
        legacyDataMessage.setDelayTimeLevel(14);
        try {
            mqProducer.send(offlineMessage);
            mqProducer.send(legacyDataMessage);
        } catch (Exception e) {
            LOGGER.error("发送离线消息失败", e);
        }
    }

    public void storeMessages(MessageDto messageDto) {
        if (null == messageDto) {
            LOGGER.error("消息为空");
            return;
        }
        Message message = new Message();
        message.setTopic(AQChatMQConstant.MQTopic.STORE_MESSAGE_TOPIC);
        message.setBody(JSONObject.toJSONString(messageDto).getBytes());
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            LOGGER.error("发送消息失败", e);
        }
    }

    public void sendLogoutMessage(String userId) {
        if (null == userId) {
            LOGGER.error("用户id为空");
            return;
        }
        Message message = new Message();
        message.setTopic(AQChatMQConstant.MQTopic.USER_LOGOUT_TOPIC);
        message.setBody(userId.getBytes());
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            LOGGER.error("发送用户退出消息失败", e);
        }
    }

    public void sendMessageToRoom(MessageDto messageDto) {
        if (null == messageDto) {
            LOGGER.error("消息为空");
            return;
        }
        Message message = new Message();
        message.setTopic(AQChatMQConstant.MQTopic.SEND_MESSAGE_TOPIC);
        message.setBody(JSONObject.toJSONString(messageDto).getBytes());
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            LOGGER.error("发送消息失败", e);
        }
    }

    public void sendLeaveRoomMsg(String userId, String roomId) {
        if (null == userId || null == roomId) {
            LOGGER.error("用户id或者房间id为空");
            return;
        }
        Message message = new Message();
        message.setTopic(AQChatMQConstant.MQTopic.LEAVE_ROOM_TOPIC);
        message.setBody(JSONObject.toJSONString(new RoomNotifyDto(roomId, userId)).getBytes());
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            LOGGER.error("发送用户退出消息失败", e);
        }
    }

    public void sendJoinRoomMsg(String userId, String roomId) {
        if (null == userId || null == roomId) {
            LOGGER.error("用户id或房间id为空");
            return;
        }
        Message message = new Message();
        message.setTopic(AQChatMQConstant.MQTopic.JOIN_ROOM_TOPIC);
        message.setBody(JSONObject.toJSONString(new RoomNotifyDto(roomId, userId)).getBytes());
        try {
            mqProducer.send(message);
            LOGGER.info("发送用户加入房间消息成功");
        } catch (Exception e) {
            LOGGER.error("发送用户加入房间消息失败", e);
        }
    }

    public void sendRecallMessage(String roomId, Long msgId, String userId) {
        if (null == roomId || null == msgId) {
            LOGGER.error("房间id或者消息id为空");
            return;
        }
        Message message = new Message();
        message.setTopic(AQChatMQConstant.MQTopic.RECALL_MESSAGE_TOPIC);
        message.setBody(JSONObject.toJSONString(new RecallMessageDto(roomId, msgId, userId)).getBytes());
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            LOGGER.error("发送撤回消息失败", e);
        }
    }

    public void aiHelper(MessageDto messageDto) {
        if (null == messageDto) {
            LOGGER.error("消息为空");
            return;
        }
        Message message = new Message();
        message.setTopic(AQChatMQConstant.MQTopic.AI_HELPER_TOPIC);
        message.setBody(JSONObject.toJSONString(messageDto).getBytes());
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            LOGGER.error("发送AI消息失败", e);
        }
    }
}
