package test.org.neusoft.neubbs.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * IRedisService 测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class IRedisServiceTestCase {
    @Autowired
    IRedisService redisService;

    @Test
    public void testSaveByKeyValue(){
        redisService.saveByKeyValue("testsave","测试");
    }

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

    @Test
    public void testRemoveByKey(){
        redisService.removeByKey("testsave");
    }

    @Test
    public void testGetValueByKey(){
        System.out.println(redisService.getValueByKey("study"));

        System.out.println(System.currentTimeMillis());
    }

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

    @Test
    public void testUpdateByKeyValue(){
        redisService.updateByKeyValue("test","new");
    }
}
