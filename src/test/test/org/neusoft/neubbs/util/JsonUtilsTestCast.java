package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.util.JsonUtils;

import java.util.Map;

/**
 * JSON 工具类 测试用例
 */
@RunWith(JUnit4.class)
public class JsonUtilsTestCast {

    /**
     * 将 Object 对象转为字符串
     */
    @Test
    public void testGetJSONStringByObject(){
        UserDO user = new UserDO();
            user.setName("hello");
            user.setRank("admin");

        System.out.println(JsonUtils.toJSONStringByObject(user));
    }

    /**
     * 将 JSON 字符串转为 Map<String, Object>
     */
    @Test
    public void testGetMapByJSONString(){
        UserDO user = new UserDO();
            user.setName("suvan");
            user.setPassword("1345");

        String json = JsonUtils.toJSONStringByObject(user);
        Map<String, Object> map = JsonUtils.toMapByJSONString(json);

        for(String key: map.keySet()){
            System.out.println(key + " : " + map.get(key));
        }
    }

    /**
     * 将 Object 对象转为 Map<String, Object>
     */
    @Test
    public void testGetMapByObject(){
        UserDO user = new UserDO();
            user.setId(1);
            user.setName("testoneuser");
            user.setAddress("中国某地");

        Map<String, Object> map = JsonUtils.toMapByObject(user);

        for(String key: map.keySet()){
            System.out.println(key + " : " + map.get(key));
        }
     }
}
