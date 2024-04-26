package com.howcode.aqchat.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.howcode.aqchat.common.constant.AQBusinessConstant;


/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-21 16:56
 */

public class IdProvider {
    private static final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    /**
     * 生成房间id
     * @return
     */
    public static String generateUserId() {
        return AQBusinessConstant.AQ_USER_PREFIX + snowflake.nextId();
    }

    /**
     * 生成房间id
     * @return
     */
    public static String generateRoomId() {
        return AQBusinessConstant.AQ_ROOM_PREFIX + snowflake.nextId();
    }

    public static Long nextId() {
        return snowflake.nextId();
    }
}
