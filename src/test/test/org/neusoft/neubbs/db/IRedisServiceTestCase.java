package test.org.neusoft.neubbs.db;

import org.junit.Test;
import org.junit.runner.RunWith;
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
public class IRedisServiceTestCase {

    @Autowired
    IRedisService redisService;

    @Autowired
    IUserService userService;

    /**
     * 测试 插入 key-value（无限期）
     */
    @Test
    public void testSaveByKeyValue(){
        redisService.saveByKeyValue("testsave","测试");
    }

    /**
     * 测试插入 key-value（指定时间，ms）
     */
    @Test
    public void testSaveByKeyValueTime(){
        redisService.saveByKeyValueTime("study", "value", 2);
        System.out.println(redisService.getValueByKey("study"));

        try{
            System.out.println("休眠2秒钟*****");
            Thread.sleep(2000);
        }catch (Exception e){}

        System.out.println("再次查询：" + redisService.getValueByKey("study"));
    }

    /**
     * 测试插入 key-value（无限期） 将 user 对象信息以 JSON 格式保存
     */
    @Test
    public void testSaveObjectForUser(){
        Map<String, String> userMap = userService.listUserInfoByName("oneuser");

            String userJSON = JsonUtils.getObjectJSONString(userMap);
            System.out.println("JSON 格式 user对象 ：" + userJSON);
    }

    /**
     * 测试根据 key ，删除 key-value
     */
    @Test
    public void testRemoveByKey(){
        redisService.removeByKey("oneuser_1507535956862");
    }

    /**
     * 测试 根据 key，获取 value
     */
    @Test
    public void testGetValueByKey(){
        System.out.println(redisService.getValueByKey("oneuser_1507535956862"));
    }

    /**
     *测试获取 key-value 剩余过期时间（ms）
     */
    @Test
    public void testGetExpreTimeByKey(){
        long hourMillis = 60 * 60 * 1000;
        redisService.saveByKeyValueTime("study", "good" , hourMillis);
        System.out.println("***********过期时间：" + redisService.getExpireTimeByKey("study"));

        try{
            System.out.println("休眠2s");
            Thread.sleep(2000);
        }catch (Exception e){}

        //-2是不存在key，-1是永久，获取的是此时此刻剩余过期秒数
        System.out.println(redisService.getExpireTimeByKey("study"));
    }

    /**
     * 测试更新 key-value（key 相同，value 直接覆盖）（无期限）
     */
    @Test
    public void testUpdateByKeyValue(){
        redisService.updateByKeyValue("test","new");
    }
}
