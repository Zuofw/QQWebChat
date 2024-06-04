package com.bronya.qqchat.service;

import com.bronya.qqchat.domain.dto.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface LoginService {
    String userLogin(String phone, String password);

    void userRegister(UserRegisterRequest registerRequest) throws InterruptedException;

    void userLogout(HttpServletRequest request);
}
