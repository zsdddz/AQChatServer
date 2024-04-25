package com.howcode.aqchat.handler.impl;

import com.howcode.aqchat.constant.AQChatConstant;
import com.howcode.aqchat.enums.AQChatExceptionEnum;
import com.howcode.aqchat.enums.MsgTypeEnum;
import com.howcode.aqchat.handler.ICmdHandler;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.model.AliOssStsDto;
import com.howcode.aqchat.utils.AliOssProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-25 13:55
 */
@Component
public class GetStsCmdHandler implements ICmdHandler<AQChatMsgProtocol.GetStsCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetStsCmdHandler.class);
    @Resource
    @Lazy
    private AliOssProvider aliOssProvider;
    @Override
    public void handle(ChannelHandlerContext ctx, AQChatMsgProtocol.GetStsCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        //获取userId判断是否登录
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf(AQChatConstant.AQBusinessConstant.USER_ID)).get();
        if (null == userId) {
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.USER_NOT_LOGIN);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        //获取阿里云临时凭证
        AliOssStsDto aliOssSts = aliOssProvider.getAliOssSts();
        if (null == aliOssSts) {
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.GET_STS_FAILED);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        int msgTypeValue = cmd.getMsgTypeValue();
        String msgTypeByCode = MsgTypeEnum.getMsgTypeByCode(msgTypeValue);
        if (null == msgTypeByCode){
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.MSG_TYPE_NOT_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return;
        }
        aliOssSts.setUploadPath(msgTypeByCode);
        LOGGER.info("用户{}获取阿里云临时凭证成功,凭证信息:{}", userId,aliOssSts);
        AQChatMsgProtocol.GetStsAck ack = MessageConstructor.buildGetStsAck(aliOssSts);
        ctx.writeAndFlush(ack);
    }
}
