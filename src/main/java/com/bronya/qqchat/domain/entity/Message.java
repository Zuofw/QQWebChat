package com.bronya.qqchat.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author bronya
 * @create 2024/6/5
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Serializable {
    public String from;
    public String fromName;
    public String to;
    public String text;
    public Date date;
    //图片
    public String image;
    //表情
    public String emoji;
}