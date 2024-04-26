package com.howcode.aqchat.mq;

import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.common.constant.AQChatMQConstant;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
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

    public void sendOfflineMessage(UserGlobalInfoDto userLoginInfo) {
        if (null == userLoginInfo) {
            LOGGER.error("用户离线消息为空");
            return;
        }
        Message message = new Message();
        message.setBody(JSONObject.toJSONString(userLoginInfo).getBytes());
        message.setTopic(AQChatMQConstant.MQTopic.OFFLINE_MESSAGE_TOPIC);
        try {
            mqProducer.send(message);
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
        message.setTopic(AQChatMQConstant.MQTopic.SEND_MESSAGE_TOPIC);
        message.setBody(JSONObject.toJSONString(messageDto).getBytes());
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            LOGGER.error("发送消息失败", e);
        }
    }
}
