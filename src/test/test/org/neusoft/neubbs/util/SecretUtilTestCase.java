package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.SecretUtil;

import javax.crypto.SecretKey;

/**
 * SecretUtil 测试类
 */
@RunWith(JUnit4.class)
public class SecretUtilTestCase {
    /**
     * 测试 MD5 加密
     */
    @Test
    public void testMd5Encryp(){
        System.out.println("密文：" + SecretUtil.encryptUserPassword("hello"));
    }

    /**
     * 测试 Base64 邮箱加密与解密
     */
    @Test
    public void testEmailBase64() throws Exception {
        String plaintext = "liushuwei0925@gmail.com-" + System.currentTimeMillis();

        String token = SecretUtil.encryptBase64(plaintext);
        System.out.println("密文：" + token);
        System.out.println("明文：" + SecretUtil.decryptBase64(token));
    }

    /**
     * 测试 AES 加密解密
     */
    @Test
    public void testAES(){
        SecretKey secretKey = SecretUtil.getAESKey();
        System.out.println("AES 密钥：" + secretKey.toString());

        String cipherText = SecretUtil.encryptAES(secretKey, "明文");
        System.out.println("AES 密文：" + cipherText);

        String plainText = SecretUtil.decryptAES(secretKey, cipherText);
        System.out.println("AES 明文：" + plainText);
    }
}
