package test.org.neusoft.neubbs.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JsonUtil;

import java.util.List;
import java.util.Map;

/**
 * JsonUtil 测试类
 *      - 测试 toJsonString()
 *      - 测试 toMapByJsonString()
 *      - 测试 toMapByObject()
 *      - 测试 toListByJsonArrayString()
 *      - 测试 isExistIntElement()
 *      - 测试 getIntElementIndex()
 *      - 测试 getJsonArrayLength()
 */
@RunWith(JUnit4.class)
public class JsonUtilTest {

    /**
     * 测试 toJsonString()
     */
    @Test
    public void testToJsonString() {
        UserDO user = new UserDO();
            user.setName("hello");
            user.setPassword("123456");

        System.out.println("UserDO Object -> " + JsonUtil.toJsonString(user));
    }

    /**
     * 测试 toMapByJsonString()
     */
    @Test
    public void testMapByJsonString() {
        UserDO user = new UserDO();
            user.setName("hello");
            user.setPassword("13456");

        String json = JsonUtil.toJsonString(user);
        Map<String, Object> map = JsonUtil.toMapByJsonString(json);

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "：" + entry.getValue());
        }
    }

    /**
     * 测试 toMapByObject()
     */
    @Test
    public void testGetMapByObject() {
        UserDO user = new UserDO();
            user.setName("hello");
            user.setName("123456");

        Map<String, Object> map = JsonUtil.toMapByObject(user);

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "：" + entry.getValue());
        }
     }

    /**
     * 测试 toListByJsonArrayString()
     */
    @Test
    public void testToListByJsonArrayString() {
        String idJsonArray = "[1, 2, 3, 4, 5, 6, 7]";
        List<Integer> list = JsonUtil.toListByJsonArrayString(idJsonArray);

        Assert.assertEquals(7, list.size());
        for (Integer id: list) {
            System.out.print(id + "\t");
        }
    }

    /**
     * 测试 isExistIntElement()
     */
    @Test
    public void testIsExistIntElement() {
        String idJsonArray = "[1, 2, 3, 4, 5, 6]";
        Assert.assertTrue(JsonUtil.isExistIntElement(idJsonArray, 4));
        Assert.assertFalse(JsonUtil.isExistIntElement(idJsonArray, 100));
    }

    /**
     * 测试 getIntElementIndex()
     */
    @Test
    public void testGetIntElementIndex() {
        String idJsonArray = "[1, 2, 3, 4, 5, 6, 7]";

        //the head index start from 0
        Assert.assertEquals(4, JsonUtil.getIntElementIndex(idJsonArray, 5));
        Assert.assertEquals(-1, JsonUtil.getIntElementIndex(idJsonArray, 100));
    }

    /**
     * 测试 getJsonArrayLength()
     */
    @Test
    public void testGetJsonArrayLength() {
        String idJsonArray = "[1, 2, 3, 4, 5, 6]";
        Assert.assertEquals(6, JsonUtil.getJsonArrayLength(idJsonArray));
    }
}
