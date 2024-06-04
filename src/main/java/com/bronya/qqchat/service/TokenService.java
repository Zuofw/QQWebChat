package com.bronya.qqchat.service;

import com.bronya.qqchat.domain.bo.LoginUser;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author bronya
 * @create 2024/4/9
 * @since 1.0.0
 */
public interface TokenService {
    String createToken(LoginUser loginUser);

    LoginUser getLoginUser(HttpServletRequest request);

    void refreshToken(LoginUser loginUser);
    void removeToken(String token);
}