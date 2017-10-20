package org.neusoft.neubbs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配 工具类
 *
 * @author Suvan
 */
public class PatternUtils {

     private static final String EXIST_NU_PURE_NUMBER = "[^0-9]";
     private static final String MATCH_USERNAME = "^[A-Za-z0-9]{3,20}$";
     private static final String MATCH_EMAIL =  "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  ;
     private static final String MATCH_IMAGE_SUFFIX =  ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
     private static final String COVER_IMAGE_TYPE = ".+/(JPG|jpg|PNG|png|JPEG|jpeg)";

    /**
     * 是否为纯数字检测（检测是否存在非 0 ~ 9 字符）
     *
     * @param str
     * @return 检测结果
     */
    public static Boolean isPureNumber(String str) {
        Pattern pattern = Pattern.compile(EXIST_NU_PURE_NUMBER);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            //存在非数字字符
            return false;
        }
        return true;
    }

    /**
     * 匹配用户名
     * @param username 用户名字符串
     * @return Boolean 匹配结果
     */
    public static Boolean matchUsername(String username){
        Pattern pattern = Pattern.compile(MATCH_USERNAME);
        Matcher matcher = pattern.matcher(username);

        //返回匹配结果（true-匹配，false-不匹配）
        return matcher.matches();
    }

    /**
     * 匹配邮箱
     *
     * @param email 邮箱字符串
     * @return Boolean 匹配结果
     */
    public static Boolean matchEmail(String email){
        Pattern pattern = Pattern.compile(MATCH_EMAIL);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


    /**
     * 匹配图片类型
     *
     * @param imageType
     * @return Boolean 匹配结果
     */
    public static Boolean isUserImage(String imageType){
        Pattern pattern = Pattern.compile(COVER_IMAGE_TYPE);
        Matcher matcher = pattern.matcher(imageType);

        return matcher.matches();
    }
}