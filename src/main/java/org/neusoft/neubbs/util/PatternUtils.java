package org.neusoft.neubbs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配 工具类
 */
public class PatternUtils {

     private final static String EXIST_NU_PURE_NUMBER = "[^0-9]";//等价 //D,匹配非数字
     private final static String MATCH_USERNAME = "^[A-Za-z0-9]{3,20}$";
     private final static String MATCH_EMAIL =  "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  ;

    /**
     * 是否为纯数字检测（检测是否存在非 0 ~ 9 字符）
     * @param str
     * @return
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
     * @param username
     * @return Boolean
     */
    public static Boolean matchUsername(String username){
        Pattern pattern = Pattern.compile(MATCH_USERNAME);
        Matcher matcher = pattern.matcher(username);

        return matcher.matches();//返回匹配结果
    }

    /**
     * 匹配邮箱
     * @param email
     * @return Boolean
     */
    public static Boolean matchEmail(String email){
        Pattern pattern = Pattern.compile(MATCH_EMAIL);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}