package test.org.neusoft.neubbs.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import redis.clients.jedis.Jedis;

/**
 * RedisUtils 测试用用例
 */
@RunWith(JUnit4.class)
public class RedisUtilsTestCase {
    /**
     * 测试Reids key-value 插入，查询，设置过期时间
     */
    @Test
    public void testGetKeys(){
        Jedis jedis = new Jedis("127.0.0.1",6379);

        String name = "name";
        String value = "suvan";
        System.out.println(jedis.set(name, value));
        System.out.println("key:" + name
                + "\nvalue:" + jedis.get(name)
                + "\nttl:" + jedis.ttl(name)
                + "\nexists:" + jedis.exists(name));

        jedis.expire(name, 3);
        System.out.println("现在过期时间为" + jedis.ttl(name) + "s");
        try{
            System.out.println("**********3s后重新查询*********");
            Thread.sleep(3000);
        }catch (Exception e){}

        System.out.println("key:" + name
                + "\nvalue:" + jedis.get(name)
                + "\nttl:" + jedis.ttl(name)
                + "\nexists:" + jedis.exists(name));
    }
}
