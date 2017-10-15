package org.neusoft.neubbs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {
    /**
     * 空判断（true - 为空）
     * @param str
     * @return Boolean
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
     * @param str
     * @param min
     * @param max
     * @return
     */
    public static Boolean isScope(String str, int min, int max){
        if (min <= str.length() && str.length() <= max) {
            return true; //符合条件
        } else {
            return false;
        }
    }

    /**
     * 正则判断（字符串是否满足，指定正则表达式）
     * @param str
     * @param regexp
     * @return boolean
     */
    public static Boolean isPattern(String str, String regexp){
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    /**
     * 是否过期（判断指定时间戳，是否过期，true-过期）
     * @param expireTime
     * @return boolean
     */
    public static Boolean isExpire(String expireTime){
        long now = System.currentTimeMillis();

        if (Long.parseLong(expireTime) > now) {
            return false;
        } else {
            return true; //过期
        }
    }

    /**
     * 生成邮件 HTML 字符串（构建，邮箱激活邮件内容）
     * @param url
     * @return String
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
