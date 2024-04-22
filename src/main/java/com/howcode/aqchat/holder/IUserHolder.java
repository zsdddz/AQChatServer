package com.howcode.aqchat.holder;

import com.howcode.aqchat.model.UserLoginInfoDto;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-22 11:49
 */

public interface IUserHolder {

    /**
     * 保存用户登录信息
     * @param userLoginInfoDto
     */
    void saveUserLoginInfo(UserLoginInfoDto userLoginInfoDto);

    /**
     * 获取用户登录信息
     * @param userId
     * @return
     */
    UserLoginInfoDto getUserLoginInfo(String userId);

    /**
     * 删除用户登录信息
     * @param userId
     */
    void removeUserLoginInfo(String userId);

}
