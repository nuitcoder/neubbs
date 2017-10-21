package org.neusoft.neubbs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串 工具类
 *
 * @author Suvan
 */
public class StringUtils {
    /**
     * 空判断（true - 为空）
     *
     * @param str 输入字符串
     * @return Boolean 检测结果
     */
    public static Boolean isEmpty(String str){
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 长度范围判断（ min <= str.length() <= max）
     *
     * @param str 输入字符串
     * @param min 最小值
     * @param max 最大值
     * @return Boolean 检测结果
     */
    public static Boolean isScope(String str, int min, int max){
        if (min <= str.length() && str.length() <= max) {
            //符合条件
            return true;
        } else {
            return false;
        }
    }

    /**
     * 正则判断（字符串是否满足，指定正则表达式）
     *
     * @param str 输入字符串
     * @param regexp 正则表达式
     * @return Boolean 检测结果
     */
    public static Boolean isPattern(String str, String regexp){
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    /**
     * 是否过期（判断此时是否过期，true-过期）
     *
     * @param expireTime 过期指定时间（时间戳）
     * @return Boolean 检测结果
     */
    public static Boolean isExpire(String expireTime){
        long now = System.currentTimeMillis();

        if (Long.parseLong(expireTime) > now) {
            return false;
        } else {
            //过期
            return true;
        }
    }

    /**
     * 生成邮件 HTML 字符串（构建，邮箱激活邮件内容）
     *
     * @param url 输入字符串
     * @return String 生成的HTML字符串
     */
    public static String createEmailActivationHtmlString(String url){
        StringBuffer sb = new StringBuffer();
            sb.append("<html><head></head><body><h1>Neubbs 帐号活邮件，点击激活帐号</h1><br>");
            sb.append("<a href=\"" + url + "\">");
            sb.append(url);
            sb.append("</a></body></html>");

        return sb.toString();
    }

}
