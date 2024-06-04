package com.bronya.qqchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.mapper.UserMapper;
import com.bronya.qqchat.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Resource
    private UserMapper userMapper;
}
