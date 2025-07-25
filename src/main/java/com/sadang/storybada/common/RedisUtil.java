package com.sadang.storybada.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public String getData(String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    public void saveAuthNumber(String key, String emailAuthNumber, int expiration) {
        redisTemplate.opsForValue().set(key, emailAuthNumber, expiration, TimeUnit.MILLISECONDS);
    }

}
