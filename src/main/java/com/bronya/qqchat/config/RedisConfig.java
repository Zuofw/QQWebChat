package com.bronya.qqchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @description:  Redis配置类
 * @author bronya
 * @date 2024/6/4 9:50
 * @version 1.0
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory); // 设置连接工厂
        StringRedisSerializer keySerializer = new StringRedisSerializer(); // 设置key序列化
        // 设置value序列化
        FastJson2JsonRedisSerializer<Object> valueSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setKeySerializer(keySerializer); // 设置key序列化
        redisTemplate.setHashKeySerializer(keySerializer); // 设置hash key序列化
        redisTemplate.setValueSerializer(valueSerializer); // 设置value序列化
        redisTemplate.setHashValueSerializer(valueSerializer); // 设置hash value序列化
        redisTemplate.afterPropertiesSet(); // 初始化
        return redisTemplate;
    }
}
