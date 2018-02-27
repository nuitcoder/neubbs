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
import org.neusoft.neubbs.exception.UtilClassException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密工具类
 *      - MD5 加密
 *      - Base64 转码
 *      - Base64 解码
 *
 * @author Suvan
 */
public final class SecretUtil {

    private SecretUtil() { }

    /**
     * MD5 加密
     *      - MD5 消息摘要算法
     *
     * @param plainText 明文
     * @return String MD5 密文
     */
    public static String encryptMd5(String plainText) {
        checkParamNotNull(plainText);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());

            return new BigInteger(1, md.digest()).toString(SetConst.REDIX_SIXTEEN);
        } catch (NoSuchAlgorithmException noe) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC1);
        }
    }

    /**
     * Base64 转码
     *
     * @param plainText 明文
     * @return String Base64 编码密文
     */
    public static String encodeBase64(String plainText) {
        checkParamNotNull(plainText);

        try {
            return Base64.getUrlEncoder().encodeToString(plainText.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ue) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC2);
        }
    }

    /**
     * Base64 解码
     *
     * @param cipherText Base64 编码密文
     * @return String 明文
     */
    public static String decodeBase64(String cipherText) {
        checkParamNotNull(cipherText);
        return new String(Base64.getDecoder().decode(cipherText));
    }



    /**
     * 生成用户信息 Token
     *      - 使用 com.auth0.jwt 库（JSON Web Token），加密生成 token
     *      - 只加密用户信息的（id，name，rank，state），使用 HS256 对称加密算法
     *
     * @param user 用户信息对象（至少 id，name，rank，state 属性不能为空）
     * @return String 密文 token
     */
    public static String generateUserInfoToken(UserDO user) {
        checkParamNotNull(user);
        if (user.getId() == null || user.getName() == null || user.getRank() == null || user.getState() == null) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC11);
        }

        Map<String, Object> headerMap = new HashMap<>(SetConst.SIZE_TWO);
            headerMap.put("alg", "HS256");
            headerMap.put("typ", "JWT");

        //set payload, use HS256 to encrypt, generate token, not set expire time
        try {
             return JWT.create()
                       .withHeader(headerMap)
                       .withIssuer("Neubbs Admin")
                       .withSubject("Neubbs User Information")
                       .withAudience("Neubbs User")
                       .withIssuedAt(new Date())
                       //.withExpiresAt(new Date(ext))
                           .withClaim(ParamConst.ID, user.getId())
                           .withClaim(ParamConst.NAME, user.getName())
                           .withClaim(ParamConst.RANK, user.getRank())
                           .withClaim(ParamConst.STATE, user.getState())
                               .sign(Algorithm.HMAC256(SetConst.JWT_TOKEN_SECRET_KEY));

        } catch (UnsupportedEncodingException e) {
            throw new UtilClassException(ApiMessage.INVALID_TOKEN).log(LogWarnEnum.US13);
        }
    }

    /**
     * 解密用户信息 Token
     *      - 解密 JWT 生成的用户信息 Token, 获取 UserDO 对象
     *
     * @param token 密文 token
     * @return UserDO 用户信息对象（包含 id，name，rank，state 属性）
     */
    public static UserDO decryptUserInfoToken(String token) {
        checkParamNotNull(token);

        DecodedJWT decodedJWT;
        try {
            //decrypt HS256
             JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SetConst.JWT_TOKEN_SECRET_KEY)).build();

            //decoding Base64
            decodedJWT = verifier.verify(token);
        } catch (UnsupportedEncodingException | TokenExpiredException e) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC10);
        }

        //Get User information(id, name, rank ,state)
        UserDO user = new UserDO();
            user.setId(decodedJWT.getClaim(ParamConst.ID).asInt());
            user.setName(decodedJWT.getClaim(ParamConst.NAME).asString());
            user.setRank(decodedJWT.getClaim(ParamConst.RANK).asString());
            user.setState(decodedJWT.getClaim(ParamConst.STATE).asInt());

        return user;
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 检擦参数不为空
     *      - 若为空则抛出异常
     *
     * @param param 参数
     */
    private static <T> void checkParamNotNull(T param) {
        if (param == null) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC12);
        }
    }
}
