package com.bronya.qqchat.config;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.bronya.qqchat.constant.RedisConstants;
import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.domain.entity.Message;
import com.bronya.qqchat.service.MessageService;
import com.bronya.qqchat.service.TokenService;
import com.bronya.qqchat.util.RedisCache;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {
    static final Map<String,WebSocketSession> sessions = new ConcurrentHashMap<>();
    @Resource
    private TokenService tokenService;
    @Value("${jwt.secret}")
    private String secret;
    @Resource
    private RedisCache redisCache;
    private final static String LOGIN_USER_KEY = "login_user_key";
    @Resource
    private MessageService messageService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        LoginUser loginUser = (LoginUser) session.getAttributes().get("loginUser");
//        log.info("用户{}连接",loginUser.getUserId());
//        sessions.put(loginUser.getUserId(),session);
        log.info("用户连接");
    }

    /*
     * @description:  处理消息
     * @author bronya
     * @date: 2024/6/5 17:28
    * @param session
    * @param message
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if(message.getPayloadLength() == 0) return;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 注册JavaTimeModule模块

        // 将message.getPayload()转换为Map<String, Object>
        Map<String, Object> payload = objectMapper.readValue((String) message.getPayload(), new TypeReference<Map<String, Object>>(){});

        String token = (String) payload.get("token");
        JWT jwt = JWTUtil.parseToken(token);
        String uuid = (String) jwt.getPayload(LOGIN_USER_KEY);
        LoginUser loginUser = redisCache.getCache(RedisConstants.USER_LOGIN_KEY + uuid);

        session.getAttributes().putIfAbsent("loginUser",loginUser);
        sessions.putIfAbsent(loginUser.getUserId(),session);
        Message msg = new Message();
        msg.setReaded(0);
        msg.setToken(token);
        // 将payload中的content字段（一个LinkedHashMap对象）转换为JsonNode对象
        msg.setContent(objectMapper.convertValue(payload.get("content"), JsonNode.class));
        msg.setImage((String) payload.get("image"));
//        msg.setFrom((String) payload.get("from"));
        msg.setFrom(((LoginUser) session.getAttributes().get("loginUser")).getUserId());
        msg.setTo((String) payload.get("to"));
        msg.setDate(LocalDateTime.parse((String) payload.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        log.info("接收到消息：{}",msg);
        messageService.save(msg); // 保存Message对象到数据库



        // 从数据库返回的Message对象中获取id字段

        sendMessageToUser(msg.getTo(),msg);
    }

    private void sendMessageToUser(String userId, Message message) {
        WebSocketSession session = sessions.get(userId);
        if(session != null && session.isOpen()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule()); // 注册JavaTimeModule模块
                //删除 token 字段
//                message.setToken(null);
                // 将整个Message对象转换为JSON字符串
                String payload = objectMapper.writeValueAsString(message);
                TextMessage textMessage = new TextMessage(payload);
                log.info("发送消息：{}",textMessage.getPayload());
                session.sendMessage(textMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * @description:  处理传输错误
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()) {
            session.close();
        }
        log.error("用户{}连接出错",session.getAttributes().get("loginUser"));
        sessions.remove(session.getAttributes().get("loginUser"));
    }

    /*
     * @description:  关闭连接时触发的方法，移除用户的session信息
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        LoginUser loginUser = (LoginUser) session.getAttributes().get("loginUser");
        log.info("用户{}断开连接",loginUser.getUserId());
        sessions.remove(loginUser.getUserId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }



}