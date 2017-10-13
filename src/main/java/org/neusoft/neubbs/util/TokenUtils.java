package org.neusoft.neubbs.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.neusoft.neubbs.constant.account.TokenInfo;
import org.neusoft.neubbs.entity.UserDO;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token 工具类
 */
public class TokenUtils {

    /**
     * 根据 UserDO 对象，构建token
     * @param user
     * @return
     * @throws Exception
     */
    public static String createToken(UserDO user) throws Exception{
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
                                .withClaim(TokenInfo.CLAIM_ID, user.getId())
                                .withClaim(TokenInfo.CLAIM_NAME, user.getName())
                                .withClaim(TokenInfo.CLAIN_RANK, user.getRank())
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
    public static UserDO verifyToken(String token, String secretKey){
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
        Claim idClaim = decodedJWT.getClaim(TokenInfo.CLAIM_ID);
        Claim nameClaim = decodedJWT.getClaim(TokenInfo.CLAIM_NAME);
        Claim rankClaim = decodedJWT.getClaim(TokenInfo.CLAIN_RANK);

        UserDO user = new UserDO();
            user.setId(idClaim.asInt());
            user.setName(nameClaim.asString());
            user.setRank(rankClaim.asString());

        return user;
    }
}
