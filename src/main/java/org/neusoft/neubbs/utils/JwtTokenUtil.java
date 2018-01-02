package org.neusoft.neubbs.utils;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.exception.TokenErrorException;
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
public final class JwtTokenUtil {

    private JwtTokenUtil() { }

    private static final String JWT = "JWT";
    private static final String HS256 = "HS256";

    private static final String HEADER = "Header";
    private static final String HEADER_TYP = "typ";
    private static final String HEADER_ALG = "alg";

    private static final String SET_ISSUER = "neubbs";
    private static final String SET_SUBJECT =  "www.neubbs.com";
    private static final String SET_AUDIENCE = "count@neubbs.com";


    /**
     * JWT 创建 token（传入 UserDO 对象）
     *
     * @param user 用户对象
     * @return String 加密后密文
     */
    public static String createToken(UserDO user) {
        Map<String, Object> headerMap = new HashMap<>(SetConst.SIZE_TWO);
            headerMap.put(HEADER_ALG, HS256);
            headerMap.put(HEADER_TYP, JWT);

        //set issuance and expiration time (expired can not be decrypted)
        long iat = System.currentTimeMillis();

        //long ext = iat + EXPIRE_TIME_ONE_DAY;
        //long ext = iat + 1;

        //set playload, use HS256 to encrypt, generate token, set no expire time
        try {
             return com.auth0.jwt.JWT.create()
                        .withHeader(headerMap)
                        .withIssuer(SET_ISSUER)
                        .withSubject(SET_SUBJECT)
                        .withAudience(SET_AUDIENCE)
                        .withIssuedAt(new Date(iat))
                        //.withExpiresAt(new Date(ext))
                            .withClaim(ParamConst.ID, user.getId())
                            .withClaim(ParamConst.NAME, user.getName())
                            .withClaim(ParamConst.RANK, user.getRank())
                            .withClaim(ParamConst.STATE, user.getState())
                                .sign(Algorithm.HMAC256(SetConst.JWT_TOKEN_SECRET_KEY));

        } catch (UnsupportedEncodingException e) {
            throw new TokenErrorException(ApiMessage.INVALID_TOKEN).log(LogWarn.ACCOUNT_16);
        }
    }

    /**
     * JWT 解密 token （传入 token 和 密钥，得到 UserDO 对象）
     *
     * @param token 密文
     * @param secretKey 解密密钥
     * @return UserDO 用户对象
     */
    public static UserDO verifyToken(String token, String secretKey) {
        JWTVerifier verifier;
        DecodedJWT decodedJWT;
        try {
            //decrypt HS256
             verifier = com.auth0.jwt.JWT.require(Algorithm.HMAC256(secretKey)).build();

            //decoding Base64
            decodedJWT = verifier.verify(token);

        } catch (UnsupportedEncodingException | TokenExpiredException e) {
            return null;
        }

        //Get User data
        Claim idClaim = decodedJWT.getClaim(ParamConst.ID);
        Claim nameClaim = decodedJWT.getClaim(ParamConst.NAME);
        Claim rankClaim = decodedJWT.getClaim(ParamConst.RANK);
        Claim stateClaim = decodedJWT.getClaim(ParamConst.STATE);

        UserDO user = new UserDO();
            user.setId(idClaim.asInt());
            user.setName(nameClaim.asString());
            user.setRank(rankClaim.asString());
            user.setState(stateClaim.asInt());

        return user;
    }
}
