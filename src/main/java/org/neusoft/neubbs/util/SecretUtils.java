package org.neusoft.neubbs.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 加密 工具类
 *
 * @author Suvan
 */
public class SecretUtils {

    private final static Integer MDT_STRING_LENGTH = 32;

    /**
     * 加密 MD5（消息摘要算法）
     *
     * @param plainText 明文（待加密的字符串）
     * @return String 密文
     */
    public static String encryptMD5(String plainText){
        byte [] secretBytes = null;
        try{
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        }catch (NoSuchAlgorithmException noe){
            //找不到指定算法
            return null;
        }catch (Exception e){
            return null;
        }

        StringBuilder md5Code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        //如果生成数字未满 32位，需要前面补0
        for(int i = 1; i < MDT_STRING_LENGTH - md5Code.length(); i++){
            md5Code.insert(0, "0");
        }

        return md5Code.toString();
    }

    /**
     * 加密用户面膜（二重 MD5 加密）
     *
     * @param password 用户密码
     * @return String 密文
     */
    public static String encryptUserPassword(String password){
        //一次 密码 MD5 加密
        String ciphertext = encryptMD5(password);
        String salt = password;

        //加密后的密文 + 原密码
        StringBuffer sb = new StringBuffer();
            sb.append(ciphertext);
            sb.append(salt);

        //二次 MD5 加密
        return encryptMD5(sb.toString());
    }

    /**
     * 加密 Base64 （用于邮箱激活，生成 token）
     *
     * @param email 用户邮箱
     * @return String 密文
     */
    public static String encryptBase64(String email){
        String token = null;
        try{
            token = Base64.getUrlEncoder().encodeToString(email.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ue) {
            return null;
        }

        return token;
    }

    /**
     * 解密 Base64（用于邮箱激活，token 解密）
     *
     * @param token Base64密文
     * @return String 明文
     */
    public static String decryptBase64(String token){
        return new String(Base64.getDecoder().decode(token));
    }
}
