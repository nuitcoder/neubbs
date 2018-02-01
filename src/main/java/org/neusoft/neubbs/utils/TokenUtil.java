package org.neusoft.neubbs.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.TokenErrorException;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token 工具类
 *      - 生成用户信息 Token
 *      - 解密用户信息 Token
 *
 * @author Suvan
 */
public final class TokenUtil {

    private TokenUtil() { }

    /**
     * 生成用户信息 Token
     *      - 使用 com.auth0.jwt 库（JSON Web Token），加密生成 token
     *      - 只加密用户信息的（id，name，rank，state），使用 HS256 对称加密算法
     *
     * @param user 用户信息对象（至少 id，name，rank，state 属性不能为空）
     * @return String 密文token
     */
    public static String generateUserInfoToken(UserDO user) {
        Map<String, Object> headerMap = new HashMap<>(SetConst.SIZE_TWO);
            headerMap.put("alg", "HS256");
            headerMap.put("typ", "JWT");

        //set payload, use HS256 to encrypt, generate token, not set expire time
        try {
             return JWT.create()
                       .withHeader(headerMap)
                       .withIssuer("neubbs")
                       .withSubject("www.neubbs.com")
                       .withAudience("count@neubbs.com")
                       .withIssuedAt(new Date(System.currentTimeMillis()))
                       //.withExpiresAt(new Date(ext))
                           .withClaim(ParamConst.ID, user.getId())
                           .withClaim(ParamConst.NAME, user.getName())
                           .withClaim(ParamConst.RANK, user.getRank())
                           .withClaim(ParamConst.STATE, user.getState())
                               .sign(Algorithm.HMAC256(SetConst.JWT_TOKEN_SECRET_KEY));

        } catch (UnsupportedEncodingException e) {
            throw new TokenErrorException(ApiMessage.INVALID_TOKEN).log(LogWarnEnum.US13);
        }
    }

    /**
     * 解密用户信息 Token
     *      - 解密 JWT 生成的用户信息 Token, 获取 UserDO 对象
     *
     * @param token 密文token
     * @return UserDO 用户信息对象（包含 id，name，rank，state 属性）
     */
    public static UserDO decryptUserInfoToken(String token) {
        DecodedJWT decodedJWT;
        try {
            //decrypt HS256
             JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SetConst.JWT_TOKEN_SECRET_KEY)).build();

            //decoding Base64
            decodedJWT = verifier.verify(token);
        } catch (UnsupportedEncodingException | TokenExpiredException e) {
            return null;
        }

        //Get User information(id, name, rank ,state)
        UserDO user = new UserDO();
            user.setId(decodedJWT.getClaim(ParamConst.ID).asInt());
            user.setName(decodedJWT.getClaim(ParamConst.NAME).asString());
            user.setRank(decodedJWT.getClaim(ParamConst.RANK).asString());
            user.setState(decodedJWT.getClaim(ParamConst.STATE).asInt());

        return user;
    }
}
