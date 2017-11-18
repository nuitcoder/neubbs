package test.org.neusoft.neubbs.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * IRedisService 测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class RedisServiceTest {

    @Autowired
    IRedisService redisService;

    @Autowired
    IUserService userService;

    /**
     * 保存 key-value（无限期）
     */
    @Ignore
    public void testSave() {
        String key = "test";
        String value = "save";

        redisService.save(key, value);
        Assert.assertNotNull(redisService.getValue(key));
    }

    /**
     * 保存 key-value（指定过期时间）
     */
    @Ignore
    public void testSaveByKeyValueTime() {
        String key = "testExpire";
        String value = "saveExpire";
        long expireTime = 2000;

        redisService.save(key, value, expireTime);
        Assert.assertNotNull(redisService.getValue(key));
        System.out.println(redisService.getExpireTime(key) + " ms after key-value expire;");

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(redisService.getExpireTime(key), -2L);
    }


    /**
     * 删除 key-value
     */
    @Ignore
    public void testRemoveByKey() {
        String key = "testRemove";
        String value = "remove";

        redisService.save(key, value);
        redisService.remove(key);
        Assert.assertNull(redisService.getValue(key));
    }

    /**
     * 查询 value
     */
    @Test
    public void testGetValue() {
        String key = "test";
        String value = redisService.getValue(key);
        System.out.println("test: " + value);
    }

    /**
     * 查询过期时间
     */
    @Test
    public void testGetExpreTime() {
        Assert.assertEquals(redisService.getExpireTime("notest"), -2);

        String key = "testExpire";
        String value = "expire";
        long expiretime = 1000;
        redisService.save(key, value, expiretime);
        System.out.println(redisService.getExpireTime(key) + "s after expired");

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(redisService.getExpireTime(key), -2);
    }

    /**
     * 更新 key-value
     */
    @Ignore
    public void testUpdate() {
        String key = "testUpdate";
        String value = "old-value";
        redisService.save(key, value);

        String newValue = "new-value";
        redisService.update(key, newValue);

        Assert.assertEquals(redisService.getValue(key), newValue);
    }
}
