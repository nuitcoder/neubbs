package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.RandomUtil;

/**
 * 测试 RandomUtil
 *
 * @author Suvan
 */
@RunWith(JUnit4.class)
public class RamdomUtilTest {

    /**
     * 测试生成各种随机数
     */
    @Test
    public void testCreateRamdom() {
        System.out.println("获取 6 位数随机数：" + RandomUtil.getSixRandomNumber());
        System.out.println("获取指定返回随机数（5 ~ 18）：" + RandomUtil.getRandomNumberByScope(5, 18));
        System.out.println("获取指定随机字符串（12位数）：" + RandomUtil.getRandomString(12));
    }
}
