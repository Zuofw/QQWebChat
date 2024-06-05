package com.bronya.qqchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bronya.qqchat.domain.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("select id from message where msg_id = #{msgId}")
    Long getByMsgId(String msgId);
}
