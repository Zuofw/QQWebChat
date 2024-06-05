package com.bronya.qqchat.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName(value = "message")
public class Message implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField(value = "msg_id")
    public String msgId;
    @TableField(value = "from_user_id")
    public String from;
    @TableField(value = "to_user_id")
    public String to;
    @TableField(value = "content")
    public JsonNode content;
    @TableField(value = "readed")
    public Integer readed;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDateTime date;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDateTime createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDateTime updateTime;
}