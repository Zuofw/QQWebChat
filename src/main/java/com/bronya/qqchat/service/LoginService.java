package com.bronya.qqchat.service;

import com.bronya.qqchat.domain.dto.UserRegisterRequest;
import com.bronya.qqchat.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LoginService {
    String userLogin(String phone, String password);

    void userRegister(UserRegisterRequest registerRequest) throws InterruptedException, IOException;

    void userLogout(HttpServletRequest request);


}
