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
        System.out.println("密文：" + SecretUtils.passwordMD5Encrypt("hello"));
    }

    /**
     * 测试 Base64 邮箱加密与解密
     */
    @Test
    public void testEmailBase64(){
        String plaintext = "liushuwei0925@gmail.com-" + System.currentTimeMillis();

        String token = SecretUtils.base64Encrypt(plaintext);
        System.out.println("密文：" + token);
        System.out.println("明文：" + SecretUtils.base64Decrypt(token));
    }
}
