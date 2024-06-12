package com.bronya.qqchat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bronya.qqchat.domain.entity.Message;

import java.util.List;

public interface MessageService extends IService<Message> {
    List<Message> getByUserId(String userId);
}
