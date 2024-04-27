package com.howcode.aqchat.service.dao.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-27 12:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("aq_user")
public class AqUser {
    @TableId
    private String userId;
    @TableField("user_name")
    private String userName;
    @TableField("user_avatar")
    private String userAvatar;
}
