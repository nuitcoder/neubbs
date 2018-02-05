package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

/**
 * 字符串 工具类
 *
 * @author Suvan
 */
public final class StringUtil {

    private StringUtil() { }

    private static NeubbsConfigDO neubbsConfig;

    static {
        neubbsConfig = new NeubbsConfigDO();

        Resource resource = new ClassPathResource("/neubbs.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);

            neubbsConfig.setFtpIp(props.getProperty("ftp.ip"));
            neubbsConfig.setNginxUrl(props.getProperty("nginx.url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public static String generateActivationMailHtmlContent(String url) {
        return "<html><head></head><body><h1>Neubbs 帐号活邮件，点击激活帐号</h1><br><a href=\""
                + url + "\">" + url + "</a></body></html>";
    }

    /**
     * 获取当天 24 点 时间戳
     *
     * @return String 24点时间戳
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
     * 获取相隔时间（格式：xxx 天）
     *
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return String 相隔天数（今天 or xxx 天前）
     */
    public static String getSeparateDay(long startTime, long endTime) {
        int separateDay = (int) ((startTime - endTime) / SetConst.ONE_DAY_MS);
        return separateDay == 0 ? "今天" : separateDay + " 天前";
    }

    /**
     * 补全前后斜杠（\）
     *      - 用于补全 URL 路径
     *
     * @param str 输入字符串
     * @return String 补全结果
     */
    public static String completeBeforeAfterSprit(String str) {
        char firstChar = str.charAt(0);
        char lastChar = str.charAt(str.length() - 1);

        StringBuilder sb = new StringBuilder(str);
        sb.insert(0, firstChar == '/' ? "" : "/");
        sb.append(lastChar == '/' ? "" : "/");

        return sb.toString();
    }

    /**
     * 拼接用户头像 URL
     *      - userInfoMap 中获取 id + name 拼接用户个人文件夹
     *      - userInfoMap 中获取 avator 拼接用户头像 URL
     *      - 支持 FTP or http
     *
     * @param userInfoMap 用户信息键值对
     * @param urlType URL类型（支持：ftp 和 http[目前仅使用此]）
     * @return String 用户头像 URL）
     */
    public static String spliceUserAvatorUrl(Map<String, Object> userInfoMap, String urlType) {
        StringBuilder avatorFtpUrl = new StringBuilder();
        if (ParamConst.HTTP.equals(urlType)) {
            avatorFtpUrl.append(getNginxHttpUrlPreFix());
        } else {
            //default
            avatorFtpUrl.append(getFtpUrlPreFix());
        }

        String imageFileName = (String) userInfoMap.get(ParamConst.AVATOR);
        if (isUserAvatorDefault(imageFileName)) {
            avatorFtpUrl.append(getUserDefaultPath() + imageFileName);
        } else {
            avatorFtpUrl.append(
                    getUserPersonalPath((Integer) userInfoMap.get(ParamConst.ID),
                            (String) userInfoMap.get(ParamConst.NAME))
            );

            avatorFtpUrl.append("avator/" + imageFileName);
        }

        return avatorFtpUrl.toString();
    }
    private static String getFtpUrlPreFix() {
        return "ftp://" + neubbsConfig.getFtpIp() + "/";
    }
    private static String getNginxHttpUrlPreFix() {
        return neubbsConfig.getNginxUrl();
    }
    private static String getUserDefaultPath() {
        return "user/default/";
    }
    private static String getUserPersonalPath(int id, String username) {
        return "user" + "/" + id + "-" + username + "/";
    }
    private static boolean isUserAvatorDefault(String imageFileName) {
        return "default-avator-min.jpeg".equals(imageFileName);
    }
}
