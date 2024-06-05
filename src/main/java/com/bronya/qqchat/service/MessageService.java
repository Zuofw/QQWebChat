package com.bronya.qqchat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bronya.qqchat.domain.entity.Message;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

public interface MessageService extends IService<Message> {
    Long getByMsgId(String msgId);
}
