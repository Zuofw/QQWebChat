# 如果不存在qq_chat数据库就创建
CREATE DATABASE IF NOT EXISTS qq_chat;
# 使用qq_chat数据库
USE qq_chat;
# 如果不存在user表就创建,如果存在就删除，重新创建
DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user(
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
    username VARCHAR(20) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    phone VARCHAR(11) NOT NULL COMMENT '手机号登录凭证,以及添加好友',
    head_image VARCHAR(100) COMMENT '头像，存的是路径', # TODO 后续改成不可为空
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT '用户表';