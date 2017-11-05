package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * IRedisService 接口实现类
 *
 * @author Suvan
 */
@Service("redisServiceImpl")
public class RedisServiceImpl implements IRedisService {

    private StringRedisTemplate redisTemplate;

    /**
     * Constructor
     */
    @Autowired
    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void save(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    @Override
    public void remove(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    @Override
    public String getValue(String key) {
       return redisTemplate.opsForValue().get(key);
    }

    @Override
    public long getExpireTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    @Override
    public void update(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
