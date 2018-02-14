package test.org.neusoft.neubbs.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.SecretUtil;

/**
 * SecretUtil 测试类
 *      - 测试 encryptMd5()
 *      - 测试 encodeBase64()
 *      - 测试 decodeBase64()
 *      - 测试 generateUserInfoToken()
 *      - 测试 generateUserInfoToken() 和 decryptUserInfoToken()
 */
@RunWith(JUnit4.class)
public class SecretUtilTest {

    /**
     * 测试 encryptMd5()
     */
    @Test
    public void testEncryptMd5() {
        String plainText = "Hello World!";
        String cipherText = SecretUtil.encryptMd5(plainText);

        System.out.println("MD5 encrypt \"" + plainText + "\" -> " + cipherText);
        Assert.assertEquals("ED076287532E86365E841E92BFC50D8C", cipherText.toUpperCase());
    }

    /**
     * 测试 encodeBase64()
     */
    @Test
    public void testEncodeBase64() {
        String plainText = "Hello World!";
        String cipherText = SecretUtil.encodeBase64(plainText);

        System.out.println("Base64 encode \"" + plainText + "\" -> " + cipherText);
        Assert.assertEquals("SGVsbG8gV29ybGQh", cipherText);
    }

    /**
     * 测试 decodeBase64()
     */
    @Test
    public void testDecodeBase64() {
        String cipherText = "SGVsbG8gV29ybGQh";
        String plainText = SecretUtil.decodeBase64(cipherText);

        System.out.println("Base64 decode \"" + cipherText + "\" -> " + plainText);
        Assert.assertEquals("Hello World!", plainText);
    }

    /**
     * 测试 generateUserInfoToken() 和 decryptUserInfoToken()
     */
    @Test
    public void testGenerateAndDecryptForUserInfoToken() {
        UserDO user = new UserDO();
            user.setId(5);
            user.setName("suvan");
            user.setRank("admin");
            user.setState(SetConst.ACCOUNT_ACTIVATED_STATE);

        String userInfoToken = SecretUtil.generateUserInfoToken(user);

        UserDO decryptUser = SecretUtil.decryptUserInfoToken(userInfoToken);
        Assert.assertEquals(decryptUser, user);
        System.out.println(decryptUser.toString());
        System.out.println(userInfoToken);
    }
}

