package com.howcode.aqchat.handler.at;

import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.model.MessageDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 16:13
 */
@Component
public class HandlerFactory implements ApplicationContextAware,InitializingBean {
    private Handler handler;

    private ApplicationContext applicationContext;

    public void handleMessage(MessageDto messageDto) {
        MessageDto messageDtoCopy = new MessageDto();
        BeanUtils.copyProperties(messageDto,messageDtoCopy);
        //处理消息内容
        String messageExt = messageDtoCopy.getMessageExt();
        String[] extArgs = messageExt.split(AQBusinessConstant.AI_SPACE_CONTENT_TAG);
        if (extArgs.length > 1) {
            messageDtoCopy.setMessageExt(extArgs[0]);
            messageDtoCopy.setMessageContent(extArgs[1]);
        }
        handler.handleMessage(messageDtoCopy);
    }

    private void init(){
        Map<String, Handler> handlerMap = applicationContext.getBeansOfType(Handler.class);
        Set<Handler> handlerSet = new HashSet<>(handlerMap.values());
        handlerSet.forEach(h -> {
            if (handler == null) {
                handler = h;
            }else {
                handler.setHandler(h);
            }
        });
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
