package org.neusoft.neubbs.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配 工具类
 *      - 是否为纯数字
 *      - 是否为纯英文
 *      - 匹配用户名
 *      - 匹配邮箱
 *      - 匹配图片类型
 *      - 匹配话题类型
 *
 * 【注意】该类大部分函数，主要用于请求参数检查工具类（ParamValidateUtil.java）,反射调用
 *
 * @author Suvan
 */
public final class PatternUtil {

    private PatternUtil() { }

    private static final String NO_NUMBER = "[^0-9]";
    private static final String NO_ENGLISH = "^[A-Za-z]+$";

    private static final String USERNAME_FORMAT = "^[A-Za-z0-9]{3,20}$";
    private static final String EMAIL_FORMAT = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)"
                                                + "+[a-zA-Z]{2,}$";
    private static final String USER_AVATAR_FORMAT = ".+/(JPG|jpg|PNG|png|JPEG|jpeg)";

    private static final String CHINESE_AND_ENGLISH_TYPE  = "^[\\u4e00-\\u9fa5a-zA-Z]+$";


    /**
     * 是否为纯数字
     *      - 检测是否存在非 0 ~ 9 字符
     *
     * @param str 纯数字类型字符串
     * @return boolean 检测结果
     */
    public static boolean isPureNumber(String str) {
        //there are other characters, no is pure number
        return !Pattern.compile(NO_NUMBER).matcher(str).find();
    }

    /**
     * 是否为纯英文
     *      - 支持大小写
     *      - 匹配由 26 个英文字母组成的字符串
     *      - 不能存在空格，以及其他符号
     *
     * @param str 匹配结果
     * @return boolean 匹配结果（true-是，false-否）
     */
    public static boolean isPureEnglish(String str) {
        return match(str, NO_ENGLISH);
    }

    /**
     * 匹配用户名
     *
     * @param username 用户名字符串
     * @return boolean 匹配结果
     */
    public static boolean matchUsername(String username) {
        return match(username, USERNAME_FORMAT);
    }

    /**
     * 匹配邮箱
     *
     * @param email 邮箱字符串
     * @return boolean 匹配结果
     */
    public static boolean matchEmail(String email) {
        return match(email, EMAIL_FORMAT);
    }


    /**
     * 匹配图片类型
     *
     * @param imageType 图片类型字符串
     * @return boolean 匹配结果
     */
    public static boolean matchUserImage(String imageType) {
        return match(imageType, USER_AVATAR_FORMAT);
    }

    /**
     * 匹配话题类型
     *
     * @param category 话题分类
     * @return boolean 匹配结果
     */
    public static boolean matchTopicCategory(String category) {
       return match(category, CHINESE_AND_ENGLISH_TYPE);
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 匹配器
     *      - 传入字符串与正则表达式，判断是否匹配
     *
     * @param str 需匹配字符串
     * @param regexp 正则表达式（匹配规则）
     * @return 匹配结果（true-成功匹配，false-不匹配）
     */
    private static boolean match(String str, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
