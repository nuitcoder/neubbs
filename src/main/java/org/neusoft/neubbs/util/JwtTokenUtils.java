package org.neusoft.neubbs.util;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.entity.UserDO;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token 工具类
 *
 * @author Suvan
 */
public class JwtTokenUtils {

    private final static String JWT = "JWT";
    private final static String HS256 = "HS256";

    private final static String HEADER = "Header";
    private final static String HEADER_TYP = "typ";
    private final static String HEADER_ALG = "alg";


    private final static String SET_ISSUER = "neubbs";
    private final static String SET_SUBJECT =  "www.neubbs.com";
    private final static String SET_AUDIENCE = "count@neubbs.com";

    /**
     * Claim参数（保存用户信息）
     */
    private final static  String CLAIM_NAME = "name";
    private final static String CLAIM_ID = "id";
    private final static String CLAIN_RANK = "rank";
    private final static String CLAIN_EMAIL = "email";

    /**
     * JWT 创建 token（传入 UserDO 对象）
     *
     * @param user 用户对象
     * @return String 加密后密文
     * @throws Exception
     */
    public static String createToken(UserDO user) throws Exception{
        Map<String,Object> headerMap = new HashMap<String, Object>(2);
            headerMap.put(HEADER_ALG, HS256);
            headerMap.put(HEADER_TYP, JWT);

        //签发时间,与过期时间（过期无法解密）
        long iat = System.currentTimeMillis();
        long ext = iat + SecretInfo.EXPIRETIME_SERVEN_DAY;
        //long ext = iat + 1;//测试过期token是否无效

        //设置Playload,且使用HS256加密,生成token
        String token  = com.auth0.jwt.JWT.create()
                                            .withHeader(headerMap)
                                            .withIssuer(SET_ISSUER)
                                            .withSubject(SET_SUBJECT)
                                            .withAudience(SET_AUDIENCE)
                                            .withIssuedAt(new Date(iat))
                                          //.withExpiresAt(new Date(ext))   //永久有效，不需要过期时间
                                                .withClaim(CLAIM_ID, user.getId())
                                                .withClaim(CLAIM_NAME, user.getName())
                                                .withClaim(CLAIN_RANK, user.getRank())
                                                    .sign(Algorithm.HMAC256(SecretInfo.TOKEN_SECRET_KEY));

        return token;
    }

    /**
     * JWT 解密 token （传入 token 和 密钥，得到 UserDO 对象）
     *
     * @param token 密文
     * @param secretKey 解密密钥
     * @return UserDO 用户对象
     * @throws Exception
     */
    public static UserDO verifyToken(String token, String secretKey){
        JWTVerifier verifier = null;
        DecodedJWT decodedJWT = null;
        try{
            //解密HS256算法
             verifier = com.auth0.jwt.JWT.require(Algorithm.HMAC256(secretKey)).build();

            //解码Base5
            decodedJWT = verifier.verify(token);

        }catch (UnsupportedEncodingException ue){
            return null;
        } catch (TokenExpiredException tee) {      //token过期
            return null;
        }

        //获取 Playload 的 username
        Claim idClaim = decodedJWT.getClaim(CLAIM_ID);
        Claim nameClaim = decodedJWT.getClaim(CLAIM_NAME);
        Claim rankClaim = decodedJWT.getClaim(CLAIN_RANK);

        UserDO user = new UserDO();
            user.setId(idClaim.asInt());
            user.setName(nameClaim.asString());
            user.setRank(rankClaim.asString());

        return user;
    }
}
