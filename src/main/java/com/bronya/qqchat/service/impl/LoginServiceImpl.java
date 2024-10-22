package com.bronya.qqchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bronya.qqchat.constant.ImageConstants;
import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.domain.dto.UserRegisterRequest;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.domain.enums.ErrorCodeEnum;
import com.bronya.qqchat.exception.BusinessException;
import com.bronya.qqchat.service.LoginService;
import com.bronya.qqchat.service.TokenService;
import com.bronya.qqchat.service.UserService;
import com.bronya.qqchat.util.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author bronya
 * @create 2024/4/9
 * @since 1.0.0
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Resource
    private UserService userService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private TokenService tokenService;

    @Resource
    private ResourceLoader resourceLoader;
    @Override
    public String userLogin(String phone, String userPassword) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone, userPassword);
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception ignored) {
        }
        if (authentication == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED, "账号或密码错误");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        System.out.println(loginUser);
        return tokenService.createToken(loginUser);
    }

    @Override
    public void userRegister(UserRegisterRequest RegisterRequest) throws InterruptedException, IOException {
        String phone = RegisterRequest.getPhone();
        if (userService.getOne(new QueryWrapper<User>().eq("phone", phone)) != null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED, "账号已存在");
        }
        String resourcesPath = resourceLoader.getResource("classpath:").getURI().getPath();
        User user = User.builder()
                .phone(phone)
                .userName(RegisterRequest.getUsername())
                .password(SecurityUtils.encodePassword(RegisterRequest.getPassword()))
                .headImage("1")
                .build();
        userService.save(user);
    }

    @Override
    public void userLogout(HttpServletRequest request) {
        log.info("logout");
        String token = request.getHeader("Authorization");
        log.info("这是Au" + token);
        tokenService.removeToken(token);
    }

}