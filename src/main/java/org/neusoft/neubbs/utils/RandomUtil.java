package org.neusoft.neubbs.utils;

import java.util.Random;

/**
 * 随机数 工具类
 *
 * @author Suvan
 */
public final class RandomUtil {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int NINE = 9;
    private static final int TEN = 10;
    private static final int TWENTY_SIX = 26;
    private static final int SIXTY_FIVE = 65;
    private static final int NINETH_SEVEN = 97;
    private static final int TEN_THOUSAND = 10000;

    private RandomUtil() {

    }

    /**
     * 获取6位数随机数
     *
     * @return Integer 整型6位数随机数
     */
    public static Integer getSixRandomNumber() {
        return  (int) ((Math.random() * NINE + ONE) * TEN_THOUSAND);
    }

    /**
     * 获取指定范围的随机数
     *
     * @param min 最小范围
     * @param max 最大范围
     * @return Integer 生成随机数
     */
    public static Integer getRandomNumberByScope(int min, int max) {
        Random random = new Random();

        //.nextInt(可能出现的数字，从0开始的)，例如：.nextInt(99)  生成  0 <= number < 99
        // 0~99区间.nextInt(100),  1~100区间.nextInt(99) + 1，64~128区间.nextInt(65) + 64
        return random.nextInt(max - min + ONE) + min;
    }

    /**
     * 获取指定长度随机字符串（a-z, A-Z, 0-9）
     *
     * @param len 字符串长度
     * @return String 随机字符串
     */
    public static String getRandomString(int len) {
        StringBuilder sb = new StringBuilder();

        int point;
        int ascii;
        for (int i = 0; i < len; i++) {
            //生成 “min <= 随机数 <= max ” 的随机数   int num = min + (int)(Math.random() * (max-min+1))
            point = 1 + (int) (Math.random() * THREE);

            if (point == ONE) {
                //数字
                sb.append((int) (Math.random() * TEN));
            } else if (point == TWO) {
                //小写字母(97 - 122)
                ascii = NINETH_SEVEN + (int) (Math.random() * TWENTY_SIX);
                sb.append((char) ascii);
            } else if (point == THREE) {
                //大写字母（65 - 90）
                ascii = SIXTY_FIVE + (int) (Math.random() * TWENTY_SIX);
                sb.append((char) ascii);
            }
        }

        return sb.toString();
    }
}
