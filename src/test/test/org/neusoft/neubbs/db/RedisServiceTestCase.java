package test.org.neusoft.neubbs.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * IRedisService 测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class RedisServiceTestCase {

    @Autowired
    IRedisService redisService;

    @Autowired
    IUserService userService;

    /**
     * 测试 插入 key-value（无限期）
     */
    @Test
    public void testSaveByKeyValue(){
        redisService.save("testsave","测试");
    }

    /**
     * 测试插入 key-value（指定时间，ms）
     */
    @Test
    public void testSaveByKeyValueTime(){
        redisService.save("study", "value", 2);
        System.out.println(redisService.getValue("study"));

        try{
            System.out.println("休眠2秒钟*****");
            Thread.sleep(2000);
        }catch (Exception e){}

        System.out.println("再次查询：" + redisService.getValue("study"));
    }

    /**
     * 测试插入 key-value（无限期） 将 count 对象信息以 JSON 格式保存
     */
    @Test
    public void testSaveObjectForUser(){
        UserDO user = userService.getUserInfoByName("oneuser");
        Map<String, Object> userInfoMap = JsonUtils.toMapByObject(user);


            String userJSON = JsonUtils.toJSONStringByObject(userInfoMap);
            System.out.println("JSON 格式 user对象 ：" + userJSON);
    }

    /**
     * 测试根据 key ，删除 key-value
     */
    @Test
    public void testRemoveByKey(){
        redisService.remove("oneuser_1507535956862");
    }

    /**
     * 测试 根据 key，获取 value
     */
    @Test
    public void testGetValueByKey(){
        System.out.println(redisService.getValue("oneuser_1507535956862"));
    }

    /**
     *测试获取 key-value 剩余过期时间（ms）
     */
    @Test
    public void testGetExpreTimeByKey(){
        long hourMillis = 60 * 60 * 1000;
        redisService.save("study", "good" , hourMillis);
        System.out.println("***********过期时间：" + redisService.getExpireTime("study"));

        try{
            System.out.println("休眠2s");
            Thread.sleep(2000);
        }catch (Exception e){}

        //-2是不存在key，-1是永久，获取的是此时此刻剩余过期秒数
        System.out.println(redisService.getExpireTime("study"));
    }

    /**
     * 测试更新 key-value（key 相同，value 直接覆盖）（无期限）
     */
    @Test
    public void testUpdateByKeyValue(){
        redisService.update("test","new");
    }
}
