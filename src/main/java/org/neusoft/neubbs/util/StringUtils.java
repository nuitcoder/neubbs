package org.neusoft.neubbs.util;

/**
 * 字符串工具类
 */
public class StringUtils {
    /**
     * 空判断
     * @param str
     * @return boolean
     */
    public static boolean isEmpty(String str){
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
