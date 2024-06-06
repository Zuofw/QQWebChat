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

import java.io.IOException;


@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private LoginService loginService;
    @Resource
    private UserService userService;
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginRequest loginRequest) {
        return ResultUtils.success(loginService.userLogin(loginRequest.getPhone(), loginRequest.getPassword()));
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody UserRegisterRequest registerRequest) throws InterruptedException, IOException {
        loginService.userRegister(registerRequest);
        return ResultUtils.success(null);
    }
    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request){
        log.info("logout");
        loginService.userLogout(request);
        return ResultUtils.success("退出成功");
    }
//    @PostMapping("/update") //只改个人信息
//    public Result<?> update(@RequestBody UserInfoRequest userInfoRequest){
//        userService.updateUser(userInfoRequest);
//        return ResultUtils.success("修改成功");
//    }
    @GetMapping("/getUserByName")
    public Result<?> getUserByName(String name){
        return ResultUtils.success(userService.getUserByName(name));
    }
//
//    @PostMapping("/updateHeadImage")
//    public Result<?> updateHeadImage(@RequestPart("image") MultipartFile image){
//        userService.updateHeadImage(image);
//        return ResultUtils.success("修改成功");
//    }
//
//    @GetMapping("/getUserInfo")
//    public Result<UserInfo> getUserInfo(HttpServletRequest request){
//        return ResultUtils.success(userService.getUserInfo(request));
//    }
//    @GetMapping("/getUserHeadImage")
//    public Result<String> getUserHeadImage(HttpServletRequest request){
//        return ResultUtils.success(loginService.getUserHeadImage(request));
//    }
    @GetMapping("/getFriendList")
    public Result<?> getFriendList(){
        return ResultUtils.success(userService.getFriendList());
    }
//    @PostMapping("/updateMessage") //是否
//    public Result<?> updateMessage(@RequestBody User user){
//        userService.updateMessage(user);
//        return ResultUtils.success("修改成功");
//    }
    @PostMapping("/getMessageByFriendId")
    public Result<?> getMessageById(@RequestParam String friendId){
        return ResultUtils.success(userService.getMessageById(friendId));
    }
    @PostMapping("/updateMessageReadedByMsgId")
    public Result<?> updateMessageReadedByMsgId(@RequestParam String msgId){
        userService.updateMessageReadedByMsgId(msgId);
        return ResultUtils.success("修改成功");
    }

}
