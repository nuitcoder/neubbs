package org.neusoft.neubbs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配 工具类
 */
public class PatternUtils {

     private final static String EXIST_NU_NUMBER_CHAR = "[^0-9]";//等价 //D,匹配非数字

    /**
     * 正则检测，输入字符串，是否为纯数字
     *
     * @param str
     * @return
     */
    public static Boolean pureNumber(String str) {
        Pattern pattern = Pattern.compile(EXIST_NU_NUMBER_CHAR);

        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            //存在非数字字符
            return false;
        }
        return true;
    }
}