package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.MapFilterUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试 MapFilterUtil
 *
 * @author Suvan
 */
@RunWith(JUnit4.class)
public class MapFilterUtilTest {
    /**
     * 测试删除多个 Key
     */
    @Test
    public void testRemoveKeys() {
        Map<String, Object> map = new HashMap<>();
            map.put("id", 1);
            map.put("name", "姓名");
            map.put("age", 16);
            map.put("address", "中国");

        MapFilterUtil.removeKeys(map, new String[] {"id", "age"});

        System.out.println(map);
    }
}
