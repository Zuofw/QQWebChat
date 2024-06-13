package com.bronya.qqchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bronya.qqchat.domain.entity.Message;
import com.bronya.qqchat.mapper.MessageMapper;
import com.bronya.qqchat.service.MessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author bronya
 * @create 2024/6/5
 * @since 1.0.0
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Override
    public List<Message> getByTo(String userId) {
        return messageMapper.getByTo(userId);
    }
}