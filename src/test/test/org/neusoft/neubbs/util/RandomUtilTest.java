package test.org.neusoft.neubbs.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.UtilClassException;
import org.neusoft.neubbs.utils.RandomUtil;

/**
 * RandomUtil 工具类
 *      - 测试 generateRandomNumbers()
 *      - 测试 generateRandomString()
 *
 * @author Suvan
 */
@RunWith(JUnit4.class)
public class RandomUtilTest {

    /**
     * 测试 generateRandomNumbers()
     */
    @Test
    public void testGenerateRandomNumbers() {
        int min;
        int max;
        for (int i = 0; i < 10000; i++) {
            min = i;
            max = i * i;
            try {
                int random = RandomUtil.generateRandomNumbers(min, max);
                Assert.assertTrue((min <= random) && (random <= max));
            } catch (UtilClassException uce) {
                Assert.assertEquals(LogWarnEnum.UC8, uce.getLog());
            }
        }
    }

    /**
     * 测试 generateRandomString()
     */
    @Test
    public void testGenerateRandomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 1000; i++) {
            sb.append(RandomUtil.generateRandomString(i));
            Assert.assertEquals(i, sb.length());
            sb.setLength(0);
        }
    }
}
