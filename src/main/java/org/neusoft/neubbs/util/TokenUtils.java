package org.neusoft.neubbs.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.constant.secret.JWTTokenSecret;
import org.neusoft.neubbs.entity.token.TokenDO;

import java.io.UnsupportedEncodingException;
import java.util.Date;
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
    public static TokenDO createToken(String username) throws Exception{ //设置Header
        Map<String,Object> headerMap = new HashMap<String, Object>();
            headerMap.put(TokenInfo.HEADER_ALG,TokenInfo.HS256);
            headerMap.put(TokenInfo.HEADER_TYP,TokenInfo.JWT);

        //签发时间,与过期时间
        long iat = System.currentTimeMillis();
        long ext = iat + TokenInfo.EXPIRETIME_SERVEN_DAY;//过期则无法解密
        //long ext = iat + 1;//测试过期token是否无效

        //设置Playload,且使用HS256加密,生成token
        String token  = JWT.create()
                            .withHeader(headerMap)
                            .withIssuer(TokenInfo.SET_ISSUER)
                            .withSubject(TokenInfo.SET_SUBJECT)
                            .withAudience(TokenInfo.SET_AUDIENCE)
                            .withIssuedAt(new Date(iat))
                            .withExpiresAt(new Date(ext))
                                .withClaim(TokenInfo.CLAIM_USERNAME, username)
                                    .sign(Algorithm.HMAC256(JWTTokenSecret.SECRET_KEY));

       //构建Token实体类
       TokenDO tokenDO = new TokenDO();
            tokenDO.setTokenname(username);
            tokenDO.setExpireTime(ext);
            tokenDO.setToken(token);

        return tokenDO;
    }

    /**
     * 根据密钥，解密token，获取用户名
     *
     * @param token
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static String verifyToken(String token, String secretKey){
        JWTVerifier verifier = null;
        DecodedJWT decodedJWT = null;
        try{
            //解密HS256算法
             verifier = JWT.require(Algorithm.HMAC256(secretKey))
                            .build();

            //解码Base5
            decodedJWT = verifier.verify(token);
        }catch (UnsupportedEncodingException ue){}
         catch (TokenExpiredException tee){
            //token过期
            return null;
        }

        //获取 Playload 的 username
        Claim claim = decodedJWT.getClaim(TokenInfo.CLAIM_USERNAME);
        return claim.asString();
    }
}
