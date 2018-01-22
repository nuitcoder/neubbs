package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * ICacheService 接口实现类
 *
 * @author Suvan
 */
@Service("redisServiceImpl")
public class CacheServiceImpl implements ICacheService {

    private StringRedisTemplate redisTemplate;

    /**
     * Constructor
     */
    @Autowired
    public CacheServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveUserEmailKey(String emailKey) {
        redisTemplate.opsForValue().set(
                emailKey, SetConst.VALUE_MAIL_SEMD_INTERVAL,
                SetConst.EXPIRE_TIME_SIXTY_SECOND_MS, TimeUnit.MILLISECONDS
        );
    }

    @Override
    public long getEmailKeyExpireTime(String emailKey) {
        return redisTemplate.getExpire(emailKey, TimeUnit.MILLISECONDS);
    }
}
