package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * IRedisService接口 实现类
 */
@Service("redisServiceImpl")
public class RedisServiceImpl implements IRedisService{
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void saveByKeyValue(String key, String value){
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    @Override
    public void saveByKeyValueTime(String key, String value, long time){//有效时间后过期
        redisTemplate.opsForValue().set(key, value , time, TimeUnit.MILLISECONDS);//时间戳，毫秒
    }

    @Override
    public void removeByKey(String key){
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    @Override
    public String getValueByKey(String key){
       ValueOperations<String, String> vo = redisTemplate.opsForValue();

       return vo.get(key);
    }

    @Override
    public long getExpireTimeByKey(String key){
        return redisTemplate.getExpire(key,TimeUnit.MILLISECONDS);//返回毫秒类型
    }

    @Override
    public void updateByKeyValue(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }
}
