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
    public void testCreateRandom() {
        System.out.println("生成随机数：" + RandomUtil.generateRandomNumbers(299, 300));
        System.out.println("生成随机字符串：" + RandomUtil.generateRandomString(12));
    }
}
