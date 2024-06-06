package com.bronya.qqchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bronya.qqchat.constant.ImageConstants;
import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.domain.dto.UserInfoRequest;
import com.bronya.qqchat.domain.entity.Friend;
import com.bronya.qqchat.domain.entity.Message;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.domain.vo.FriendVO;
import com.bronya.qqchat.domain.vo.UserInfo;
import com.bronya.qqchat.mapper.FriendMapper;
import com.bronya.qqchat.mapper.UserMapper;
import com.bronya.qqchat.service.TokenService;
import com.bronya.qqchat.service.UserService;
import com.bronya.qqchat.util.ImagesUtils;
import com.bronya.qqchat.util.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Resource
    private ResourceLoader resourceLoader;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TokenService tokenService;

    @Resource
    private FriendMapper friendMapper;

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

    @Override
    public String getUserByName(String name) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", name)).getPhone();
    }

    @Override
    public List<FriendVO> getFriendList() {
        String userId = SecurityUtils.getUserId();
        //通过userId查询好友列表

        List<Friend> friends = friendMapper.selectList(new QueryWrapper<Friend>().eq("user_id", userId));
        //查询出好友的名字

        List<FriendVO> collect = friends.stream()
                .map(friend -> {
                    User user = userMapper.selectById(friend.getFriendId());
                    return FriendVO.builder()
                            .friendId(user.getId().toString())
                            .friendName(user.getUserName())
                            .build();
                }).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(collect);
        return collect;
    }

    @Override
    public Page<Message> getMessageById(String toUserId) {
        String  fromUserId = SecurityUtils.getUserId();
        //获取按时间顺序的后十条消息
        Page<Message> page = new Page<>(1, 10);
        return userMapper.getMessageById(page, fromUserId, toUserId);
    }

    @Override
    public void updateMessageReadedByMsgId(Message message) {
        String fromUserId = SecurityUtils.getUserId();
        String toUserId = message.getTo();
        LocalDateTime date = message.getDate();
        log.info("fromUserId: {}, toUserId: {}, date: {}", fromUserId, toUserId, date);
        // 把所有这个时间之前的消息都设置为已读
        userMapper.updateMessageReadedByMsgId(fromUserId, toUserId, date);
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
