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

    private static final String EXIST_NO_PURE_NUMBER = "[^0-9]";
    private static final String PURE_ENGILISH = "^[A-Za-z]+$";
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
     * @return boolean 检测结果
     */
    public static boolean isPureNumber(String str) {
        Pattern pattern = Pattern.compile(EXIST_NO_PURE_NUMBER);
        Matcher matcher = pattern.matcher(str);

        //ther are other characters, no is pure number
        return !matcher.find();
    }

    /**
     * 是否为全英文检测
     *      - 支持大小写
     *      - 匹配由 26 个英文字母组成的字符串
     *      - 不能存在空格，以及其他符号
     *
     * @param str 匹配结果
     * @return boolean 匹配结果（true-是，false-否，存在非英文字符）
     */
    public static boolean isPureEngish(String str) {
        Pattern pattern = Pattern.compile(PURE_ENGILISH);
        Matcher matcher = pattern.matcher(str);

        return matcher.find();
    }

    /**
     * 匹配用户名
     *
     * @param username 用户名字符串
     * @return boolean 匹配结果
     */
    public static boolean matchUsername(String username) {
        Pattern pattern = Pattern.compile(MATCH_USERNAME);
        Matcher matcher = pattern.matcher(username);

        return matcher.matches();
    }

    /**
     * 匹配邮箱
     *
     * @param email 邮箱字符串
     * @return Boolean 匹配结果
     */
    public static boolean matchEmail(String email) {
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
    public static boolean matchUserImage(String imageType) {
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
    public static boolean matchTopicCategory(String category) {
       Pattern pattern = Pattern.compile(MATCH_CHINESE_AND_ENGLISH);
       Matcher matcher = pattern.matcher(category);

       return matcher.matches();
    }
}
