package com.bronya.qqchat.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Type;
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
@TableName(value = "message")
public class Message implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField(value = "from_user_id")
    public String from;
    @TableField(value = "to_user_id")
    public String to;
    @TableField(value = "content")
    public String content;
    public Integer readed;
    public Date date;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    public Date createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    public Date updateTime;
}