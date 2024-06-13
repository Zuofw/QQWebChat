package com.bronya.qqchat.config;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.bronya.qqchat.constant.RedisConstants;
import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.domain.entity.Message;
import com.bronya.qqchat.domain.vo.MessageVO;
import com.bronya.qqchat.service.MessageService;
import com.bronya.qqchat.service.TokenService;
import com.bronya.qqchat.service.UserService;
import com.bronya.qqchat.util.RedisCache;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {
    static final Map<String,WebSocketSession> sessions = new ConcurrentHashMap<>();

    //定时任务
    @Resource
    private TokenService tokenService;
    @Value("${jwt.secret}")
    private String secret;
    @Resource
    private UserService userService;
    @Resource
    private RedisCache redisCache;
    private final static String LOGIN_USER_KEY = "login_user_key";
    @Resource
    private MessageService messageService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 其他代码...
        log.info("连接成功");

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        // 启动一个定时任务，每隔10秒查询一次Redis中的键
        executorService.scheduleAtFixedRate(() -> {
            log.info("开始执行定时任务");
            LoginUser loginUser = (LoginUser) session.getAttributes().get("loginUser");
            if(loginUser != null) {
                String userId = loginUser.getUserId();
                String key = RedisConstants.USER_ACTIVE + userId; // 需要查询的键
                Object value = redisCache.getCache(key);
                // 处理查询到的值...
                if(value == null) {
                    log.info("用户{}已经断开连接",value);
                    WebSocketSession webSocketSession = sessions.get(userId);
                    if(webSocketSession != null && webSocketSession.isOpen()) {
                        try {
//                            sessions.remove(userId);
                            webSocketSession.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    log.info("定时任务执行完毕");
                } else {

                }
            }

        }, 0, 60, TimeUnit.SECONDS); // 10秒后开始执行，每隔10秒执行一次

        executorService.scheduleAtFixedRate(()->{
            LoginUser loginUser = (LoginUser) session.getAttributes().get("loginUser");
            if(loginUser == null) {
                log.info("还未初始化用户信息");
            } else {
                log.info("检测是否有新消息");
                String userId = loginUser.getUserId();
                List<Message> byUserId = messageService.getByTo(userId);
                log.info("这是我查出来的消息{}",byUserId);
                for (Message message : byUserId) {
                    message.setIsSend(1);
                    MessageVO messageVO = MessageVO.builder()
                            .id(message.getFrom())
                            .date(message.getDate())
                            .content(message.getContent())
                            .image(message.getImage())
                            .target(message.getTarget())
                            .check(message.getReaded() == 1)
                            .build();
                    sendMessageToUser(userId,messageVO);
                    messageService.updateById(message);
                }
                log.info("发送未读消息完成");
            }

        } ,0, 5, TimeUnit.SECONDS);


        session.getAttributes().putIfAbsent("executorService",executorService);
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
        log.info("message.getPayload():{}",message.getPayload());
        Map<String, Object> payload = objectMapper.readValue((String) message.getPayload(), new TypeReference<Map<String, Object>>(){});

        log.info("payload:{}",payload);
        String token = (String) payload.get("token");
        String ping = (String) payload.get("type");
        log.info("这是token:{}",token);

        if(ping != null) {
            log.info("这是type:{}",ping);
            LoginUser loginUser = (LoginUser)session.getAttributes().get("loginUser");
            String userId = loginUser.getUserId();
            redisCache.setExpireCache(RedisConstants.USER_ACTIVE + userId, userId, 60, TimeUnit.SECONDS); // 10秒后删除
        } else {
            if(token != null) {
                JWT jwt = JWTUtil.parseToken(token);
                String uuid = (String) jwt.getPayload(LOGIN_USER_KEY);
                LoginUser loginUser = redisCache.getCache(RedisConstants.USER_LOGIN_KEY + uuid);
                session.getAttributes().putIfAbsent("loginUser",loginUser);
                sessions.putIfAbsent(loginUser.getUserId(),session);
//                Long cnt = 0L;
//                log.info("用户已经只走token{}次",++cnt);

            } else {

                Message msg = new Message();
                msg.setReaded(0);
                msg.setIsSend(0);
//            msg.setToken(token);
                // 将payload中的content字段（一个LinkedHashMap对象）转换为JsonNode对象
                //不要转义字符
                msg.setContent((String) payload.get("content"));
                msg.setImage((String) payload.get("image"));
//        msg.setFrom((String) payload.get("from"));
                msg.setFrom((String) payload.get("id"));
                msg.setTarget((String) payload.get("target"));
                msg.setDate(LocalDateTime.parse((String) payload.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                MessageVO messageVO = MessageVO.builder()
                        .id(msg.getFrom())
                        .date(msg.getDate())
                        .content(msg.getContent())
                        .image(msg.getImage())
                        .target(msg.getTarget())
                        .check(false)
                        .build();
                log.info("接收到消息：{}",msg);
                if( sendMessageToUser(msg.getTarget(),messageVO)) {
                    msg.setIsSend(1);
                }
                messageService.save(msg); // 保存Message对象到数据库



                // 从数据库返回的Message对象中获取id字段


            }
        }





    }

    private boolean sendMessageToUser(String userId, MessageVO messageVO) {
        WebSocketSession session = sessions.get(userId);
        if(session != null && session.isOpen()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule()); // 注册JavaTimeModule模块
                //删除 token 字段
//                message.setToken(null);
                // 将整个Message对象转换为JSON字符串
                String payload = objectMapper.writeValueAsString(messageVO);
                TextMessage textMessage = new TextMessage(payload);
                log.info("发送消息：{}",messageVO);
                session.sendMessage(textMessage);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("用户{}不在线",userId);
        }
        return false;
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
        // 其他代码...
        //如果用户主动退出，移除用户的session信息
        log.info("用户{}已经退出",session.getAttributes().get("loginUser"));
        LoginUser loginUser = (LoginUser) session.getAttributes().get("loginUser");
        if(loginUser != null) {
            redisCache.deleteCache(RedisConstants.USER_ACTIVE + loginUser.getUserId());
            sessions.remove(loginUser.getUserId());
        }

        // 在连接关闭时停止定时任务
        ScheduledExecutorService executorService = (ScheduledExecutorService) session.getAttributes().get("executorService");
        if (executorService != null) {
            executorService.shutdown();
        }
    }
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }



}