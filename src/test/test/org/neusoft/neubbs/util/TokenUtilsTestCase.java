package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.util.TokenUtils;
import org.neusoft.neubbs.util.utilentity.TokenDO;

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
        TokenDO tokenDO = null;
        String tokenName = null;
        try {
            //获取加密token
            tokenDO = TokenUtils.createToken("suvan");
            System.out.println("token:" + tokenDO.getToken());

            //休眠1s，让token失效
            Thread.sleep(1000);

            //根据密钥，解密token，获取用户名
            tokenName = TokenUtils.verifyToken(tokenDO.getToken(), TokenInfo.SECRET_KEY);
            if(tokenName == null){
                System.out.println("token已经过期，无法解密");
            }else{
                System.out.println("tokenname: " + tokenName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
