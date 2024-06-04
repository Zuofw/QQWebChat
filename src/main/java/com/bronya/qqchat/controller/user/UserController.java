package com.bronya.qqchat.controller.user;

import com.bronya.qqchat.domain.dto.UserLoginRequest;
import com.bronya.qqchat.domain.dto.UserRegisterRequest;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.domain.vo.Result;
import com.bronya.qqchat.service.LoginService;
import com.bronya.qqchat.service.UserService;
import com.bronya.qqchat.util.ResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginRequest loginRequest) {
        return ResultUtils.success(loginService.userLogin(loginRequest.getPhone(), loginRequest.getPassword()));
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody UserRegisterRequest registerRequest) throws InterruptedException {
        loginService.userRegister(registerRequest);
        return ResultUtils.success(null);
    }
    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request){
        log.info("logout");
        loginService.userLogout(request);
        return ResultUtils.success("退出成功");
    }

}
