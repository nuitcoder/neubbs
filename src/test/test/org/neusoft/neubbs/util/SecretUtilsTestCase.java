package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.util.SecretUtils;

/**
 * SecretUtils 测试类
 */
@RunWith(JUnit4.class)
public class SecretUtilsTestCase {
    /**
     * 测试 MD5 加密
     */
    @Test
    public void testMd5Encryp(){
        System.out.println(SecretUtils.passwordMD5Encrypt("hello"));
    }
}
