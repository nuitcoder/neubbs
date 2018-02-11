package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.UtilClassException;

/**
 * 随机数 工具类
 *      - 生成随机数
 *      - 生成随机字符串
 *
 * @author Suvan
 */
public final class RandomUtil {

    private RandomUtil() { }

    /**
     * 生成随机数
     *      - Math.random() 能生成 >= 0 且 < 1 的双精度伪随机数
     *      - 指定 min（最小值），max（最大值）min <= random numbers <= max
     *      - 参考公式：(随机数 * (max - min + 1)) + 1
     *      - 需满足 min > max，否则抛出异常
     *      - int 极限值范围
     *          - 最大值：Integer.MAX_VALUE = 0x7fffffff = 2147483647
     *          - 最小值：Integer.MIN_VALUE = 0x80000000 =-2147483648
     *
     * @return int 生成随机数
     */
    public static int generateRandomNumbers(int min, int max) {
        if (min > max) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC8);
        }
        //return new Random().nextInt(max - min + 1) + min;
        return  (int) ((Math.random() * (max - min + 1))) + min;
    }

    /**
     * 生成随机字符串
     *      - 随机字符串取值（a ~ z, A ` Z, 0 ~ 9）
     *      - 思路
     *          1. 随机生成 1 ~ 3 范围内的数字
     *          2. 根据生成数字，判断执行哪个流程（1 - 生成小写字母，2 - 生成大写字母，3 - 生成数字）
     *          3. 执行流程中，随机生成指定范围的 ASCII 码（97 ~ 122 小写字母）（65 ~ 90 大写字母）
     *          4. 将其转换成 char 类型，单个字符，追加至结果字符串
     *
     * @param len 字符串长度
     * @return String 随机字符串
     */
    public static String generateRandomString(int len) {
        StringBuilder resultString = new StringBuilder();

        int point;
        int tempASCII;
        for (int i = 0; i < len; i++) {
            point = (int) (Math.random() * SetConst.POINT_THREE) + SetConst.POINT_ONE;

            //lower case (97 ~ 122), upper case (65 ~ 90), number (0 ~ 9)
            tempASCII = point == SetConst.POINT_ONE
                    ? (int) (Math.random() * SetConst.ALL_LETTER_AMOUNT) + SetConst.LOWERCASE_ASCII_MIN
                    : point == SetConst.POINT_TWO
                        ? (int) (Math.random() * SetConst.ALL_LETTER_AMOUNT) + SetConst.UPPERCASE_ASCII_MIN
                        : (int) (Math.random() * SetConst.FIGURE_MAX);

            resultString.append(tempASCII <= SetConst.FIGURE_MAX ? String.valueOf(tempASCII) : (char) tempASCII);
        }

        return resultString.toString();
    }
}
