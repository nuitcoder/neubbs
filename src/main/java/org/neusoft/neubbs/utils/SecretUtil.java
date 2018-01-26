package org.neusoft.neubbs.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 加密 工具类
 *
 * @author Suvan
 */
public final class SecretUtil {

    private SecretUtil() { }

    private static final int MDT_STRING_LENGTH = 32;
    private static final String AES_SEED = "密钥种子";

    private static final int TWO = 2;
    private static final int SIXTEEN = 16;
    private static final int ZEROXFF = 0xFF;

    /**
     * 加密 MD5（消息摘要算法）
     *      - 抛出异常，即返回 null
     *      - 如果生成数字未满 32位，需要前面补0
     *
     * @param plainText 明文（待加密的字符串）
     * @return String 密文
     */
    public static String encryptMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException noe) {
            return null;
        }

        StringBuilder md5Code = new StringBuilder(new BigInteger(1, secretBytes).toString(SIXTEEN));

        for (int i = 1; i < MDT_STRING_LENGTH - md5Code.length(); i++) {
            md5Code.insert(0, "0");
        }

        return md5Code.toString();
    }

    /**
     * 加密用户密码（二重 MD5 加密）
     *      - 一次 密码 MD5 加密
     *      - 二次 MD5 加密（初次加密密文 + salt（原密码））
     *
     * @param password 用户密码
     * @return String 密文
     */
    public static String encryptUserPassword(String password) {
        return encryptMD5(encryptMD5(password) + password);
    }

    /**
     * 加密 Base64
     *      - 用于邮件 token（用户邮箱-当天 24 点的时间戳）
     *
     * @param plainText 明文
     * @return String 密文
     */
    public static String encryptBase64(String plainText) {
        String token;
        try {
            token = Base64.getUrlEncoder().encodeToString(plainText.getBytes("utf-8"));
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
    public static String decryptBase64(String token) {
         return new String(Base64.getDecoder().decode(token));
    }

    /**
     * 获取 AES 密钥
     *      1.密钥生成
     *      2.设置密钥种子，每次生成固定密钥
     *      3.生成密钥
     *      4.获取密钥字
     *
     * @return SecretKey 密钥实例
     */
    public static SecretKey getAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            random.setSeed(AES_SEED.getBytes());
            keyGenerator.init(random);

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * AES 加密
     *      1.指定算法，获取 Cipher 对象
     *      2.初始化 Cipher，设置加密模式，传入密钥
     *      3.更新需要加密内容
     *      4.最终加密操作（可合并34步）cipher.doFinal(plainText.getBytes()
     *      5.注意：加密后的 byte 是不能直接转为 String，
     *             会报错"Input length must be multiple of 16 when decrypting with padded cipher"
     *             根据网上的方法，加密后，byte 转为16进制，再转为 String
     *
     * @param secretKey 密钥实例
     * @param plainText 明文
     * @return 密文
     */
    public static String encryptAES(SecretKey secretKey, String plainText) {
        String cipherText = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            cipher.update(plainText.getBytes());
            byte[] cipherByte = cipher.doFinal();

            cipherText = parseTo16String(cipherByte);

        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException
                | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return cipherText;
    }

    /**
     * 解密 AES
     *      1.指定算法，获取 Cipher 对象
     *      2.初始化 Cipher，设置解密模式，传入密钥
     *      3.将密文，从 16 进制转为2进制的 byte
     *      4.解密，存入 String
     *
     *
     * @param secretKey 密钥实例
     * @param cipherText 密文
     * @return String 明文
     */
    public static String decryptAES(SecretKey secretKey, String cipherText) {
       String plainText = null;
       try {
           Cipher cipher = Cipher.getInstance("AES");
           cipher.init(Cipher.DECRYPT_MODE, secretKey);

           byte[] plainByte = cipher.doFinal(parseHexStr2Byte(cipherText));
           plainText = new String(plainByte);

       } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException
               | InvalidKeyException | BadPaddingException e) {
           e.printStackTrace();
       }

        return plainText;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf 字节数组
     * @return String 转换后字符串
     */
    private static String parseTo16String(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & ZEROXFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制密文字符串
     * @return byte[] 2进制字符
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / TWO];
        for (int i = 0; i < hexStr.length() / TWO; i++) {
            int high = Integer.parseInt(hexStr.substring(i * TWO, i * TWO + 1), SIXTEEN);
            int low = Integer.parseInt(hexStr.substring(i * TWO + 1, i * TWO + TWO), SIXTEEN);
            result[i] = (byte) (high * SIXTEEN + low);
        }
        return result;
    }
}
