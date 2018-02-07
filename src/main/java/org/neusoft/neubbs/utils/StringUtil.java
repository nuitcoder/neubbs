package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.UtilClassException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

/**
 * 字符串工具类
 *      - 空判断
 *      - 长度范围判断
 *      - 过期判断
 *      - 生成激活邮件 HTML 内容
 *      - 生成用户头像 URL
 *      - 获取今天 24 时的时间戳
 *      - 补全前后斜杠
 *
 * @author Suvan
 */
public final class StringUtil {

    private StringUtil() { }

    private static final String NGINX_URL;

    /*
     * ***********************************************
     * 静态代码块
      *     - 读取 src/main/resources/neubbs.properties
     * ***********************************************
     */
    static {
        Resource resource = new ClassPathResource("/neubbs.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            NGINX_URL = props.getProperty("nginx.url");
        } catch (IOException ioe) {
            throw new UtilClassException(ApiMessage.UNKNOWN_ERROR).log(LogWarnEnum.UC3);
        }
    }

    /**
     * 空判断
     *
     * @param str 输入字符串
     * @return boolean 检测结果（true-为空，false-不为空）
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 长度范围判断
     *      - min <= str.length() <= max
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
     * 过期判断
     *      - 与当前系统时间判断是否过期
     *
     * @param expireTime 待判断的时间（时间戳）
     * @return boolean 检测结果（true-过期，false-不过期）
     */
    public static boolean isExpire(String expireTime) {
        return Long.parseLong(expireTime) <= System.currentTimeMillis();
    }

    /**
     * 生成激活邮件 HTML 内容
     *
     * @param url 需加入的激活链接 URL
     * @return String 激活邮件HTML内容
     */
    public static String generateActivateMailHtmlContent(String url) {
        return "<html><head></head><body><h1>Neubbs 帐号活邮件，点击激活帐号</h1><br>"
                + "<a href=\"" + url + "\">" + url + "</a></body></html>";
    }

    /**
     * 生成用户头像 URL
     *      - userInfoMap 中获取  id + name + avatar 拼接用户个人文件夹
     *      - 生成 Nginx 代理服务器（在 neubbs.properties 文件中配置 nginx.url）指向的 http 地址
     *
     * @param userInfoMap 用户信息键值对
     * @return String 用户头像网络地址
     */
    public static String generateUserAvatarUrl(Map<String, Object> userInfoMap) {
        //get user information
        int userId = (Integer) userInfoMap.get(ParamConst.ID);
        String userName = (String) userInfoMap.get(ParamConst.NAME);
        String userAvatar = (String) userInfoMap.get(ParamConst.AVATOR);


        return SetConst.USER_DEFAULT_AVATAR.equals(userAvatar)
                     ? NGINX_URL + "user/default/" + userAvatar
                     : NGINX_URL + "user" + "/" + userId + "-" + userName + "/avator/" + userAvatar;
    }

    /**
     * 获取今天 24 时的时间戳
     *
     * @return String 24时的时间戳
     */
    public static String getTodayTwentyFourClockTimestamp() {
        Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, SetConst.TIME_TWENTY_FOUR_HOUR);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);

        return String.valueOf(calendar.getTimeInMillis());
    }

    /**
     * 判断相隔天数
     *      - 开始时间 - 结束时间，相隔多少天
     *      - 格式：今天 or xxx 天前
     *
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return String 相隔天数
     */
    public static String judgeSeparateDay(long startTime, long endTime) {
        int separateDay = (int) ((startTime - endTime) / SetConst.ONE_DAY_MS);
        return separateDay == 0 ? "今天" : separateDay + " 天前";
    }

    /**
     * 补全前后斜杠
     *      - 用于补全 URL 路径，前后 \
     *
     * @param str 输入字符串
     * @return String 补全结果
     */
    public static String completeAroundSprit(String str) {
        char firstChar = str.charAt(0);
        char lastChar = str.charAt(str.length() - 1);

        StringBuilder sb = new StringBuilder(str);
            sb.insert(0, firstChar == '/' ? "" : "/");
            sb.append(lastChar == '/' ? "" : "/");

        return sb.toString();
    }
}
