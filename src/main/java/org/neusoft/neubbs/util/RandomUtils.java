package org.neusoft.neubbs.util;

/**
 * 随机数 工具类
 *
 * @author Suvan
 */
public class RandomUtils {

    /**
     * 获取6位数随机数
     *
     * @return Integer 整型6位数随机数
     */
    public static Integer getSixRandomNumber(){
        return  (int)((Math.random() * 9 + 1) * 100000);
    }
}
