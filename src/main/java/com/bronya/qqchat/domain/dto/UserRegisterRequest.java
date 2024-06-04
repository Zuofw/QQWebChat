package com.bronya.qqchat.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 〈一句话功能简述〉<br>
 * 〈注册dto〉
 *
 * @author bronya
 * @create 2024/4/9
 * @since 1.0.0
 */
@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private String phone; //手机号
}