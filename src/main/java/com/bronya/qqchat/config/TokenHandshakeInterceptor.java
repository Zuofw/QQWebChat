package com.bronya.qqchat.config;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.bronya.qqchat.constant.RedisConstants;
import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.service.TokenService;
import com.bronya.qqchat.util.RedisCache;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
public class TokenHandshakeInterceptor implements HandshakeInterceptor {
    @Resource
    private TokenService tokenService;
    @Value("${jwt.secret}")
    private String secret;
    @Resource
    private RedisCache redisCache;
    private final static String LOGIN_USER_KEY = "login_user_key";
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("握手前");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手后");
    }
}