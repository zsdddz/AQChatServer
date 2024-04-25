package com.howcode.aqchat.dao.po;

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
    private Long messageId;
    private String roomId;
    private String senderId;
    private Integer messageType;
    private String messageContent;
    private Date createTime;
}
