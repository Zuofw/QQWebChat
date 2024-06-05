package com.bronya.qqchat.config;

import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.domain.entity.Message;
import com.bronya.qqchat.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {
    static final Map<String,WebSocketSession> sessions = new ConcurrentHashMap<>();
    @Resource
    private MessageService messageService;
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
        msg.setReaded(0);
        log.info("接收到消息：{}",msg);
        sendMessageToUser(msg.getTo(),msg);
        messageService.save(msg);
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