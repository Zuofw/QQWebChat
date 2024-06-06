package com.bronya.qqchat.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bronya.qqchat.domain.dto.UserInfoRequest;
import com.bronya.qqchat.domain.entity.Message;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.domain.vo.FriendVO;
import com.bronya.qqchat.domain.vo.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends IService<User> {

    void updateUser(UserInfoRequest userInfoRequest);

    void updateHeadImage(MultipartFile imaged);

    UserInfo getUserInfo(HttpServletRequest request);

    String getUserByName(String name);

    List<FriendVO> getFriendList();

    Page<Message> getMessageById(String toUserId);

    void updateMessageReadedByMsgId(Message message);


}
