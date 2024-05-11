package com.howcode.aqchat.handler;


import com.howcode.aqchat.common.config.AQChatConfig;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.holder.IUserHolder;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.holder.GlobalChannelHolder;

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

    @Resource
    private GlobalChannelHolder channelHolder;

    @Resource
    private IUserHolder userHolder;

    private void init() {
        heartBeatTime = aqChatConfig.getHeartBeatTime();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.ALL_IDLE) {
                Long lastReadTime = (Long) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.HEART_BEAT_TIME)).get();
                long now = System.currentTimeMillis();
                if (lastReadTime != null && now - lastReadTime > heartBeatTime) {
                    //获取用户id
                    String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).get();
                    // 下线
                    Logger.info("用户{}心跳超时，发送离线消息",userId);
                    //构建离线消息
                    AQChatMsgProtocol.OfflineMsg.Builder builder = AQChatMsgProtocol.OfflineMsg.newBuilder();
                    AQChatMsgProtocol.User.Builder userBuilder = AQChatMsgProtocol.User.newBuilder();
                    userBuilder.setUserId(userId);
                    UserGlobalInfoDto userInfo = userHolder.getUserInfo(userId);
                    userBuilder.setUserAvatar(userInfo.getUserAvatar());
                    userBuilder.setUserName(userInfo.getUserName());
                    builder.setUser(userBuilder.build());
                    builder.setRoomId(channelHolder.getRoomId(userId));
                    ctx.writeAndFlush(builder.build());
                    ctx.close();
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
