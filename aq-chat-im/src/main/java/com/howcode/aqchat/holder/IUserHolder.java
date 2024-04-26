package com.howcode.aqchat.holder;


import com.howcode.aqchat.common.model.UserGlobalInfoDto;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-22 11:49
 */

public interface IUserHolder {

    /**
     * 保存用户登录信息
     * @param userGlobalInfoDto
     */
    void saveUserInfo(UserGlobalInfoDto userGlobalInfoDto);

    /**
     * 获取用户登录信息
     * @param userId
     * @return
     */
    UserGlobalInfoDto getUserInfo(String userId);

    /**
     * 删除用户登录信息
     * @param userId
     */
    void removeUserInfo(String userId);

    void offline(String userId);
}
