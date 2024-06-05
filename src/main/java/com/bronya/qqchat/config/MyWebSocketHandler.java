package com.bronya.qqchat.config;

import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.domain.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {
    static final Map<String,WebSocketSession> sessions = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LoginUser loginUser = (LoginUser) session.getAttributes().get("loginUser");
        log.info("用户{}连接",loginUser.getUserId());
        sessions.put(loginUser.getUserId(),session);
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
        Message msg = objectMapper.readValue(message.getPayload().toString(), Message.class);
        msg.setDate(new Date());
        log.info("接收到消息：{}",msg);
        sendMessageToUser(msg.getTo(),msg);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    private void sendMessageToUser(String userId, Message message) {
        WebSocketSession session = sessions.get(userId);
        if(session != null && session.isOpen()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String payload = objectMapper.writeValueAsString(message);
                TextMessage textMessage = new TextMessage(payload);
                session.sendMessage(textMessage );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}