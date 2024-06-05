package com.bronya.qqchat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bronya.qqchat.domain.dto.UserInfoRequest;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.domain.vo.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<User> {

    void updateUser(UserInfoRequest userInfoRequest);

    void updateHeadImage(MultipartFile imaged);

    UserInfo getUserInfo(HttpServletRequest request);
}
