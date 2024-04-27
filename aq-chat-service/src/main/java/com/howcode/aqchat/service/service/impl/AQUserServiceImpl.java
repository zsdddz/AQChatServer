package com.howcode.aqchat.service.service.impl;

import com.howcode.aqchat.service.dao.mapper.IAQUserMapper;
import com.howcode.aqchat.service.dao.po.AqUser;
import com.howcode.aqchat.service.service.IAQUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-27 12:42
 */
@Service
public class AQUserServiceImpl implements IAQUserService {
    @Resource
    private IAQUserMapper userMapper;
    @Override
    public AqUser getUserById(String userId) {
        return userMapper.selectById(userId);
    }
}
