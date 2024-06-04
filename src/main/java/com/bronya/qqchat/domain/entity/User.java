package com.bronya.qqchat.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *用户表实体类
 * @TableName user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "user")
public class User implements Serializable {

    /**
     * 用户序号ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;



    /** 昵称 */
    @TableField("username")
    private String userName;
    @TableField("password")
    private String password;


    /** 联系方式 */
    @TableField("phone")
    private String phone;

    /** 头像 */
    @TableField("head_image")
    private String headImage;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;



    @Serial
    @TableField(exist = false) // 该字段不是数据库字段
    //序列化
    private static final long serialVersionUID = 1L;

}
