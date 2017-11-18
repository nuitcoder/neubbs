package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JsonUtil;

import java.util.Map;

/**
 * JSON 工具类 测试用例
 */
@RunWith(JUnit4.class)
public class JsonUtilTest {

    /**
     * 将 Object 对象转为字符串
     */
    @Test
    public void testGetJSONStringByObject(){
        UserDO user = new UserDO();
            user.setName("hello");
            user.setRank("admin");

        System.out.println(JsonUtil.toJSONStringByObject(user));
    }

    /**
     * 将 JSON 字符串转为 Map<String, Object>
     */
    @Test
    public void testGetMapByJSONString(){
        UserDO user = new UserDO();
            user.setName("suvan");
            user.setPassword("1345");

        String json = JsonUtil.toJSONStringByObject(user);
        Map<String, Object> map = JsonUtil.toMapByJSONString(json);

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "：" + entry.getValue());
        }
    }

    /**
     * 将 Object 对象转为 Map<String, Object>
     */
    @Test
    public void testGetMapByObject() {
        UserDO user = new UserDO();
            user.setId(1);
            user.setName("testoneuser");

        Map<String, Object> map = JsonUtil.toMapByObject(user);

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "：" + entry.getValue());
        }
     }
}
