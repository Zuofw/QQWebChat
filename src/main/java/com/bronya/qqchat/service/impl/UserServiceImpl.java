package com.bronya.qqchat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bronya.qqchat.constant.ImageConstants;
import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.domain.dto.UserInfoRequest;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.domain.vo.UserInfo;
import com.bronya.qqchat.mapper.UserMapper;
import com.bronya.qqchat.service.TokenService;
import com.bronya.qqchat.service.UserService;
import com.bronya.qqchat.util.ImagesUtils;
import com.bronya.qqchat.util.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Resource
    private ResourceLoader resourceLoader;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TokenService tokenService;


    @Override
    public void updateUser(UserInfoRequest userInfoRequest) {
        User user = User.builder()
                        .id(Long.parseLong(Objects.requireNonNull(SecurityUtils.getUserId())))
                                .userName(userInfoRequest.getUserName())
                                        .password(SecurityUtils.encodePassword(userInfoRequest.getPassword()))
                                                .build();

        userMapper.updateById(user);

    }

    @Override
    public void updateHeadImage(MultipartFile image) {
        String headUrl = SecurityUtils.getUserId() + image.getOriginalFilename(); //头像路径
        try {
            // 获取resources目录的路径
            String resourcesPath = resourceLoader.getResource("classpath:").getURI().getPath();
            // 构建图片保存路径
            String headImageFullPath = resourcesPath + ImageConstants.HEAD_IMAGE_PATH;
            log.info("headImageFullPath: {}", headImageFullPath);
            ImagesUtils.save(headImageFullPath, Objects.requireNonNull(SecurityUtils.getLoginUser()).getUserId(), image);
            String headImageRelativePath = ImageConstants.HEAD_IMAGE_PATH + "/" + headUrl;
            User user = User.builder()
                    .id(Long.parseLong(Objects.requireNonNull(SecurityUtils.getUserId())))
                    .headImage(headImageRelativePath)
                    .build();
            userMapper.updateById(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserInfo getUserInfo(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        User user = userMapper.selectById(loginUser.getUserId());

        // 获取头像文件的相对路径
        String headImageRelativePath = user.getHeadImage();
        // 使用ClassLoader获取文件的绝对路径
        String headImagePath = ClassLoader.getSystemResource(headImageRelativePath).getPath();
        log.info("headImagePath: {}", headImagePath);
        // 将头像文件转换为Base64编码的字符串
        String headImageContent = getImageAsBase64String(headImagePath);
        System.out.println("headImageContent: " + headImageContent);
        // 设置头像内容
        return UserInfo.builder()
                .phone(user.getPhone())
                .userName(user.getUserName())
                .headImage(headImageContent)
                .build();
    }
    private String getImageAsBase64String(String imagePath) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            String encodedString = Base64.getEncoder().encodeToString(imageBytes);
            return encodedString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
