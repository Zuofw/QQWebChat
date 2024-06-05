package com.bronya.qqchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bronya.qqchat.domain.entity.Friend;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
}
