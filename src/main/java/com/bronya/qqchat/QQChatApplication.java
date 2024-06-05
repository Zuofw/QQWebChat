package com.bronya.qqchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@MapperScan(basePackages = "com.bronya.qqchat.mapper")
@EnableWebSocket
public class QQChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(QQChatApplication.class, args);
    }

}
