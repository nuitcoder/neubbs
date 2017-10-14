package org.neusoft.neubbs.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 加密 工具类
 */
public class SecretUtils {
    /**
     * MD5 加密（消息摘要算法）
     * @param plainText
     * @return String
     */
    public static String MD5Encryp(String plainText){
        byte [] secretBytes = null;
        try{
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        }catch (NoSuchAlgorithmException noe){
            return null; //找不到算法异常
        }catch (Exception e){
            return null;
        }

        String md5Code = new BigInteger(1, secretBytes).toString(16); //16进制数字

        //如果生成数字未满 32位，需要前面补0
        for(int i = 1; i < 32 - md5Code.length(); i++){
            md5Code = "0" + md5Code;
        }

        return md5Code;
    }

    /**
     *  用户密码 MD5 加密（二重 MD5 加密）
     * @param password
     * @return String
     */
    public static String passwordMD5Encrypt(String password){
        //一次 密码 MD5 加密
        String ciphertext = MD5Encryp(password);
        String salt = password;

        //加密后的密文 + 原密码
        StringBuffer sb = new StringBuffer();
            sb.append(ciphertext);
            sb.append(salt);

        //二次 MD5 加密
        return MD5Encryp(sb.toString());
    }

    /**
     *  Base64 加密 （用于邮箱激活，生成 token）
     * @param email
     * @return String
     */
    public static String base64Encrypt(String email){
        String token = null;
        try{
            token = Base64.getUrlEncoder().encodeToString(email.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ue) {
            return null;
        }

        return token;
    }

    /**
     * Base64 解密（用于邮箱激活，token 解密）
     * @param token
     * @return
     */
    public static String base64Decrypt(String token){
        return new String(Base64.getDecoder().decode(token));
    }
}
