package com.bronya.qqchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bronya.qqchat.domain.entity.Message;
import com.bronya.qqchat.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    Page<Message> getMessageById(Page<Message> page, String fromUserId, String toUserId);

    @Update("update message set readed = 1 where msg_id = #{msgId}")
    void updateMessageReadedByMsgId(String msgId);
}
