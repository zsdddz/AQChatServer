package com.howcode.aqchat.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.GeneratedMessageV3;
import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.constant.AQMessageQueueTopicConstant;
import com.howcode.aqchat.common.model.UserGlobalInfoDto;
import com.howcode.aqchat.holder.GlobalChannelHolder;
import com.howcode.aqchat.holder.IUserHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-20 0:35
 */
@Component
@ChannelHandler.Sharable
public class AQChatCommandHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AQChatCommandHandler.class);

    @Resource
    AQChatHandlerFactory commandHandlerFactory;
    @Resource
    private IUserHolder userHolder;
    @Resource
    private GlobalChannelHolder globalChannelHolder;
    @Resource
    private MQProducer mqProducer;

    /**
     * 异常或者正常断线 都会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        //获取用户id
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQBusinessConstant.USER_ID)).get();
        if (userId == null){
            ctx.close();
            return;
        }
        LOGGER.info("用户{}断开连接",userId);
        UserGlobalInfoDto userLoginInfo = userHolder.getUserLoginInfo(userId);
        userHolder.removeUserLoginInfo(userId);
        //移除用户连接以及用户所在房间
        NioSocketChannel logout = globalChannelHolder.logout(userId);

        //mq发送用户离线消息
        Message message = new Message();
        message.setBody(JSONObject.toJSONString(userLoginInfo).getBytes());
        message.setTopic(AQMessageQueueTopicConstant.OFFLINE_MESSAGE_TOPIC);
        mqProducer.send(message);
        logout.close();
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof GeneratedMessageV3){
            ICmdHandler<? extends GeneratedMessageV3> commandHandler = commandHandlerFactory.getCommandHandler(msg.getClass());
            if (commandHandler != null){
                commandHandler.handle(channelHandlerContext, castToMessage(msg));
            }
        }
    }

    private <T extends GeneratedMessageV3> T castToMessage(Object msg){
        if (msg == null){
            return null;
        }
        return (T) msg;
    }
}