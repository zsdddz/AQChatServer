package com.howcode.aqchat.service.service;

import com.howcode.aqchat.service.dao.po.AqUser;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-27 12:41
 */
public interface IAQUserService {
    AqUser getUserById(String userId);

    void saveUser(String userId, String userName, String userAvatar);
}
