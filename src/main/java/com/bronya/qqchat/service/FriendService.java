package com.bronya.qqchat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bronya.qqchat.domain.entity.Friend;

public interface FriendService extends IService<Friend> {
    void addFriend(String phone);
}
