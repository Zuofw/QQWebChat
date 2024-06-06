package com.bronya.qqchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bronya.qqchat.domain.entity.Friend;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.mapper.FriendMapper;
import com.bronya.qqchat.mapper.UserMapper;
import com.bronya.qqchat.service.FriendService;
import com.bronya.qqchat.service.UserService;
import com.bronya.qqchat.util.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author bronya
 * @create 2024/6/5
 * @since 1.0.0
 */
@Service
@Slf4j
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private FriendMapper FriendMapper;
    @Override
    public void addFriend(String phone) {
        String userId = SecurityUtils.getUserId();
        String friendId = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("phone", phone)
        ).getId().toString();
        log.info("userId: {}, friendId: {}", userId, friendId);
        Friend friend = Friend.builder()
                .userId(userId)
                .friendId(friendId)
                .build();
        FriendMapper.insert(friend);
    }
}