# 如果不存在qq_chat数据库就创建,编码为utf8mb4
CREATE DATABASE IF NOT EXISTS qq_chat DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
# 使用qq_chat数据库
USE qq_chat;
# 如果不存在user表就创建,如果存在就删除，重新创建
DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user(
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
    username VARCHAR(20) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    phone VARCHAR(11) NOT NULL COMMENT '手机号登录凭证,以及添加好友',
    head_image VARCHAR(100) COMMENT '头像暂时用不到了', # TODO 后续改成不可为空
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT '用户表';

# 如果不存在friend表就创建
DROP TABLE IF EXISTS friend;
CREATE TABLE IF NOT EXISTS friend(
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '好友关系id',
    user_id INT NOT NULL COMMENT '用户id',
    friend_id INT NOT NULL COMMENT '好友id',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT '好友关系表';
# message表
DROP TABLE IF EXISTS message;
CREATE TABLE IF NOT EXISTS message(
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '消息id',
    from_user_id INT NOT NULL COMMENT '发送者id',
    to_user_id INT NOT NULL COMMENT '接收者id',
    content TEXT NOT NULL COMMENT '消息内容',
    image TEXT COMMENT '图片', #使用Base64编码
    readed INT NOT NULL DEFAULT 0 COMMENT '是否已读',
    is_send INT NOT NULL DEFAULT 0 COMMENT '是否发送成功',
    date DATETIME NOT NULL COMMENT '发送时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT '消息表';