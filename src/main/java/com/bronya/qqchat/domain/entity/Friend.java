package com.bronya.qqchat.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName(value = "friend")
public class Friend {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "friend_id")
    private String friendId;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private String createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

}