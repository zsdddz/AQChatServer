package com.howcode.aqchat.handler.impl;


import com.howcode.aqchat.common.constant.AQBusinessConstant;
import com.howcode.aqchat.common.enums.AQChatExceptionEnum;
import com.howcode.aqchat.common.enums.MsgTypeEnum;
import com.howcode.aqchat.common.model.AliOssStsDto;
import com.howcode.aqchat.handler.AbstractCmdBaseHandler;
import com.howcode.aqchat.message.AQChatMsgProtocol;
import com.howcode.aqchat.message.MessageConstructor;
import com.howcode.aqchat.service.hepler.AliOssProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-25 13:55
 */
@Component
public class GetStsCmdHandler extends AbstractCmdBaseHandler<AQChatMsgProtocol.GetStsCmd> {
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
        String userId = verifyLogin(ctx);
        if (null == userId) {
            LOGGER.error("GetStsCmdHandler handle error, user not login");
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
        String msgTypeByCode = getMsgTypeByCode(ctx, msgTypeValue);
        if (msgTypeByCode == null) return;
        aliOssSts.setUploadPath(msgTypeByCode + "/" + getFormatTime());
        LOGGER.info("用户{}获取阿里云临时凭证成功,凭证信息:{}", userId, aliOssSts);
        AQChatMsgProtocol.GetStsAck ack = MessageConstructor.buildGetStsAck(aliOssSts);
        ctx.writeAndFlush(ack);
    }

    /**
     * 根据消息类型code获取消息类型
     *
     * @param ctx
     * @param msgTypeValue
     * @return
     */
    private static String getMsgTypeByCode(ChannelHandlerContext ctx, int msgTypeValue) {
        String msgTypeByCode = MsgTypeEnum.getMsgTypeByCode(msgTypeValue);
        if (null == msgTypeByCode) {
            AQChatMsgProtocol.ExceptionMsg exceptionMsg = MessageConstructor.buildExceptionMsg(AQChatExceptionEnum.MSG_TYPE_NOT_EXIST);
            ctx.writeAndFlush(exceptionMsg);
            return null;
        }
        return msgTypeByCode;
    }

    /**
     * 获取格式化时间
     *
     * @return 格式化时间
     */
    private static String getFormatTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(AQBusinessConstant.UPLOAD_PATH_DATE_FORMAT);
        return sdf.format(new Date());
    }

}
