package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.util.TokenUtils;

/**
 * TokenUtils类 测试用例
 */
@RunWith(JUnit4.class)
public class TokenUtilsTestCase {

    /**
     * 测试Token 加密与解密
     */
    @Test
    public void testTokenEncryptionDecrypted(){
        String token = null;
        try {
            //获取加密token
            token = TokenUtils.createToken("suvan");
            System.out.println("util: " + token);

            //根据密钥，解密token，获取用户名
            String username = TokenUtils.verifyToken(token, TokenInfo.SECRET_KEY);
            System.out.println("username: " + username);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
