package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.util.SecretUtils;

import javax.crypto.SecretKey;

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
        System.out.println("密文：" + SecretUtils.encryptUserPassword("hello"));
    }

    /**
     * 测试 Base64 邮箱加密与解密
     */
    @Test
    public void testEmailBase64(){
        String plaintext = "liushuwei0925@gmail.com-" + System.currentTimeMillis();

        String token = SecretUtils.encryptBase64(plaintext);
        System.out.println("密文：" + token);
        System.out.println("明文：" + SecretUtils.decryptBase64(token));
    }

    /**
     * 测试 AES 加密解密
     */
    @Test
    public void testAES(){
        SecretKey secretKey = SecretUtils.getAESKey();
        System.out.println("AES 密钥：" + secretKey.toString());

        String cipherText = SecretUtils.encryptAES(secretKey, "明文");
        System.out.println("AES 密文：" + cipherText);

        String plainText = SecretUtils.decryptAES(secretKey, cipherText);
        System.out.println("AES 明文：" + plainText);
    }
}
