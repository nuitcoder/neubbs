package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.UtilClassException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
     * @return String MD5密文
     */
    public static String encryptMd5(String plainText) {
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
     * @return String Base64编码密文
     */
    public static String encodeBase64(String plainText) {
        try {
            return Base64.getUrlEncoder().encodeToString(plainText.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ue) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC2);
        }
    }

    /**
     * Base64 解码
     *
     * @param cipherText Base64编码密文
     * @return String 明文
     */
    public static String decodeBase64(String cipherText) {
         return new String(Base64.getDecoder().decode(cipherText));
    }
}
