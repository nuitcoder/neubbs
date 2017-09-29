package org.neusoft.neubbs.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.neusoft.neubbs.constant.login.TokenInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Token 工具类
 *
 * @Author Suvan
 * @Date 2017-09-28-14:46
 */
public class TokenUtils {
    /**
     * 根据用户名，生成token
     *
     * @param username
     * @return
     * @throws Exception
     */
    public static String createToken(String username) throws Exception{
        //设置Header
        Map<String,Object> headerMap = new HashMap<String, Object>();
            headerMap.put(TokenInfo.HEADER_ALG,TokenInfo.HS256);
            headerMap.put(TokenInfo.HEADER_TYP,TokenInfo.JWT);


        //设置Playload,且使用HS256加密,生成token
        String token  = JWT.create()
                            .withHeader(headerMap)
                            .withIssuer(TokenInfo.SET_ISSUER)
                            .withSubject(TokenInfo.SET_SUBJECT)
                            .withAudience(TokenInfo.SET_AUDIENCE)
                            .withClaim(TokenInfo.CLAIM_USERNAME,username)
                                .sign(Algorithm.HMAC256(TokenInfo.SECRET_KEY));

        return token;
    }

    /**
     * 根据密钥，解密token，获取用户名
     *
     * @param token
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static String verifyToken(String token, String secretKey) throws Exception{
        //解密HS256算法
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                                    .build();

        //解码base64
        DecodedJWT decodedJWT = verifier.verify(token);

        //获取用户名
        Claim claim = decodedJWT.getClaim(TokenInfo.CLAIM_USERNAME);

        return claim.asString();
    }
}
