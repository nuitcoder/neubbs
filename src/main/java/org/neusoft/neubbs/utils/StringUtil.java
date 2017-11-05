package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.controller.exception.TokenExpireException;

import java.util.Calendar;

/**
 * 字符串 工具类
 *
 * @author Suvan
 */
public final class StringUtil {

    private StringUtil() { }


    /**
     * 空判断（true - 为空）
     *
     * @param str 输入字符串
     * @return boolean 检测结果
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 长度范围判断（ min <= str.length() <= max）
     *
     * @param str 输入字符串
     * @param min 最小值
     * @param max 最大值
     * @return boolean 检测结果
     */
    public static boolean isScope(String str, int min, int max) {
        return (min <= str.length() && str.length() <= max);
    }

    /**
     * 是否过期（判断此时是否过期，true-过期）
     *
     * @param expireTime 过期指定时间（时间戳）
     * @return boolean 检测结果
     * @throws Exception 所有异常
     */
    public static boolean isExpire(String expireTime) throws Exception {
        long time;
        try {
            time = Long.parseLong(expireTime);
        } catch (NumberFormatException nfe) {
            throw new TokenExpireException(ApiMessage.IVALID_TOKEN).log(LogWarn.ACCOUNT_15);
        }

        return time <= System.currentTimeMillis();
    }

    /**
     * 生成邮件 HTML 字符串（构建，邮箱激活邮件内容）
     *
     * @param url 输入字符串
     * @return String 生成的HTML字符串
     */
    public static String createEmailActivationHtmlString(String url) {
        StringBuffer sb = new StringBuffer();
            sb.append("<html><head></head><body><h1>Neubbs 帐号活邮件，点击激活帐号</h1><br>");
            sb.append("<a href=\"" + url + "\">");
            sb.append(url);
            sb.append("</a></body></html>");

        return sb.toString();
    }

    /**
     * 获取当天 24 点 时间戳
     * @return String 24点时间戳
     */
    public static String getTwentyFourClockTime() {
        Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, SetConst.TWENTY_FOUR);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);

        return String.valueOf(calendar.getTimeInMillis());
    }


}
