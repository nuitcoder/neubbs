package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * IRedisService 接口实现类
 *
 * @author Suvan
 */
@Service("redisServiceImpl")
public class RedisServiceImpl implements IRedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(String key, String value) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        vo.set(key, value);
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
       ValueOperations<String, String> vo = redisTemplate.opsForValue();

       return vo.get(key);
    }

    @Override
    public Long getExpireTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    @Override
    public void update(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
