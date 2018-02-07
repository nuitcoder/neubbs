package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.SecretUtil;

/**
 * SecretUtil 测试类
 */
@RunWith(JUnit4.class)
public class SecretUtilTest {

    /**
     * 测试 MD5 加密
     */
    @Test
    public void testEncryptMd5() {
        String plainText = "Hello World!";
        System.out.println("MD5 encrypt \"" + plainText + "\" -> " + SecretUtil.encryptMd5(plainText));
    }

    /**
     * 测试 Base64 转码
     */
    @Test
    public void testEncodeBase64() {
        String plainText = "Hello World!";
        System.out.println("Base64 encode \"" + plainText + "\" -> " + SecretUtil.encodeBase64(plainText));
    }

    /**
     * 测试 Base64 解码
     */
    @Test
    public void testDecodeBase64() {
        String cipherText = "SGVsbG8gV29ybGQh";
        System.out.println("Base64 decode \"" + cipherText + "\" -> " + SecretUtil.decodeBase64(cipherText));
    }
}
