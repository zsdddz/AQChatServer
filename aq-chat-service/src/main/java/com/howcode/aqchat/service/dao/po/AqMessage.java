package com.howcode.aqchat.service.dao.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-25 23:21
 */
@Data
@TableName("aq_message")
public class AqMessage {
    @TableId
    private String messageId;
    @TableField("room_id")
    private String roomId;
    @TableField("sender_id")
    private String senderId;
    @TableField("message_type")
    private Integer messageType;
    @TableField("message_content")
    private String messageContent;
    @TableField("message_ext")
    private String messageExt;
    @TableField("status")
    private Integer status;
    @TableField("create_time")
    private Date createTime;
}
