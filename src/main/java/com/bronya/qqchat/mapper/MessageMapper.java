package com.bronya.qqchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bronya.qqchat.domain.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("select * from message where to_user_id = #{userId} and readed = 0 and message.is_send = 0 order by create_time desc")
    List<Message> getByUserId(String userId);

}
