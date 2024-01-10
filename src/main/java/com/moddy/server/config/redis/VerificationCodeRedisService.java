package com.moddy.server.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class VerificationCodeRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final int EXPIRE_VERIFICATION_CODE_TIME = 3;

    public void saveVerificationCode(String phoneNumber, String verificationCode) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(phoneNumber, verificationCode);
        redisTemplate.expire(phoneNumber, EXPIRE_VERIFICATION_CODE_TIME, TimeUnit.MINUTES);
    }

    public boolean isVerificationCodeEmpty(String phoneNumber) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(phoneNumber) == null;
    }

    public String getVerificationCode(String phoneNumber) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(phoneNumber);
    }

    public void deleteVerificationCode(String phoneNumber) {
        redisTemplate.delete(phoneNumber);
    }
}
