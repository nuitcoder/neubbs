package org.neusoft.neubbs.utils;

import java.util.Random;

/**
 * 随机数 工具类
 *
 * @author Suvan
 */
public class RandomUtil {

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

    /**
     * 获取指定长度随机字符串（a-z, A-Z, 0-9）
     *
     * @param len 字符串长度
     * @return String 随机字符串
     */
    public static String getRandomString(int len){
        StringBuilder sb = new StringBuilder();

        int point;
        int ascii;
        for(int i = 0; i < len; i++){
            //生成 “min <= 随机数 <= max ” 的随机数   int num = min + (int)(Math.random() * (max-min+1))
            point = 1 + (int)(Math.random()*3);

            if (point == 1) {
                //数字
                sb.append((int)(Math.random() * 10));
            } else if (point == 2) {
                //小写字母(97 - 122)
                ascii = 97 + (int)(Math.random() * 26);
                sb.append((char)ascii);
            } else if (point == 3) {
                //大写字母（65 - 90）
                ascii = 65 + (int)(Math.random() * 26);
                sb.append((char)ascii);
            }
        }

        return sb.toString();
    }
}
