package com.bronya.qqchat.domain.dto;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈用户登录接收〉
 *
 * @author bronya
 * @create 2024/6/4
 * @since 1.0.0
 */
@Data
public class UserLoginRequest {
    private String phone;
    private String password;
}