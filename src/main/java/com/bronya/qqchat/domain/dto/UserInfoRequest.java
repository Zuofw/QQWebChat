package com.bronya.qqchat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author bronya
 * @create 2024/6/4
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {
    private String userName;
    private String phone;
    private String password;
}