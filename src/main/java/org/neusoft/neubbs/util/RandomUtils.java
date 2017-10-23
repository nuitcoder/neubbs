package org.neusoft.neubbs.util;

import java.util.Random;

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

    /**
     * 获取指定范围的随机数
     *
     * @param min 最小范围
     * @param max 最大范围
     * @return Integer 生成随机数
     */
    public static Integer getRandomNumberByScope(int min, int max){
        Random random = new Random();

        //.nextInt(可能出现的数字，从0开始的)，例如：.nextInt(99)  生成  0 <= number < 99
        // 0~99区间.nextInt(100),  1~100区间.nextInt(99) + 1，64~128区间.nextInt(65) + 64
        return random.nextInt(max - min + 1) + min;
    }
}
