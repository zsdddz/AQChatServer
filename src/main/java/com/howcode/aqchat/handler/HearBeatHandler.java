package com.howcode.aqchat.handler;

import com.howcode.aqchat.config.AQChatConfig;
import com.howcode.aqchat.constant.AQChatConstant;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description: 心跳处理器
 * @date 2024-04-21 22:23
 */
@Component
@ChannelHandler.Sharable
public class HearBeatHandler extends ChannelInboundHandlerAdapter implements InitializingBean {

    private static final Logger Logger = LoggerFactory.getLogger(HearBeatHandler.class);

    private Long heartBeatTime = 3000L;

    @Resource
    private AQChatConfig aqChatConfig;

    private void init(){
        heartBeatTime = aqChatConfig.getHeartBeatTime();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.READER_IDLE) {
                Logger.info("读空闲");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                Logger.info("进入写空闲");
            } else if (event.state() == IdleState.ALL_IDLE) {
                Long lastReadTime = (Long) ctx.channel()
                        .attr(AttributeKey.valueOf(AQChatConstant.AQBusinessPrefix.HEART_BEAT_TIME)).get();
                long now = System.currentTimeMillis();

                if (lastReadTime != null && now - lastReadTime > heartBeatTime) {
                    // 下线

                }

            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        Logger.info("HearBeatHandler init Success, heartBeatTime: {}ms", heartBeatTime);
    }
}
