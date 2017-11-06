package org.neusoft.neubbs.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配 工具类
 *
 * @author Suvan
 */
public final class PatternUtil {

    private PatternUtil() { }

    private static final String EXIST_NU_PURE_NUMBER = "[^0-9]";
    private static final String MATCH_USERNAME = "^[A-Za-z0-9]{3,20}$";
    private static final String MATCH_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)"
                                                + "+[a-zA-Z]{2,}$";
    private static final String MATCH_IMAGE_SUFFIX =  ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
    private static final String COVER_IMAGE_TYPE = ".+/(JPG|jpg|PNG|png|JPEG|jpeg)";
    private static final String MATCH_CHINESE = "[\\u4e00-\\u9fa5]";
    private static final String MATCH_CHINESE_AND_ENGLISH = "^[\\u4e00-\\u9fa5a-zA-Z]+$";


    /**
     * 是否为纯数字检测（检测是否存在非 0 ~ 9 字符）
     *
     * @param str 纯数字类型字符串
     * @return Boolean 检测结果
     */
    public static Boolean isPureNumber(String str) {
        Pattern pattern = Pattern.compile(EXIST_NU_PURE_NUMBER);
        Matcher matcher = pattern.matcher(str);

        //match.find() = true, 表示存在非数字字符
        return !matcher.find();
    }

    /**
     * 匹配用户名
     *
     * @param username 用户名字符串
     * @return Boolean 匹配结果
     */
    public static Boolean matchUsername(String username) {
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
    public static Boolean matchEmail(String email) {
        Pattern pattern = Pattern.compile(MATCH_EMAIL);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


    /**
     * 匹配图片类型
     *
     * @param imageType 图片类型字符串
     * @return Boolean 匹配结果
     */
    public static Boolean matchUserImage(String imageType) {
        Pattern pattern = Pattern.compile(COVER_IMAGE_TYPE);
        Matcher matcher = pattern.matcher(imageType);

        return matcher.matches();
    }

    /**
     * 匹配话题类型
     *
     * @param category 话题分类
     * @return Boolean 匹配结果
     */
    public static Boolean matchTopicCategory(String category) {
       Pattern pattern = Pattern.compile(MATCH_CHINESE_AND_ENGLISH);
       Matcher matcher = pattern.matcher(category);

       return matcher.matches();
    }
}
