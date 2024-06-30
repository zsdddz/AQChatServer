package com.howcode.aqchat.handler.at.chain;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.common.utils.IdProvider;
import com.howcode.aqchat.handler.at.Handler;
import com.howcode.aqchat.mq.MqSendingAgent;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 16:08
 */
@Component
public class XQHandler extends Handler {
    @Resource
    private MqSendingAgent mqSendingAgent;
    @Override
    public void handleMessage(MessageDto messageDto) {
        //判断是否@了AI
        String messageExt = messageDto.getMessageExt();
        if ((AQBusinessConstant.AT + AQBusinessConstant.AI_HELPER_ID).equals(messageExt)) {
            //拷贝一份消息
            MessageDto aiMessageDto = new MessageDto();
            BeanUtils.copyProperties(messageDto, aiMessageDto);
            //覆盖消息Id
            aiMessageDto.setMessageId(IdProvider.nextId()+"");
            //修改消息内容 去掉@AI
            aiMessageDto.setMessageContent(messageDto.getMessageContent().replace((AQBusinessConstant.AT + AQBusinessConstant.AI_HELPER_NAME), ""));
            mqSendingAgent.aiHelper(aiMessageDto);
        }else {
            //继续传递
            if (handler != null) {
                handler.handleMessage(messageDto);
            }
        }
    }
}
