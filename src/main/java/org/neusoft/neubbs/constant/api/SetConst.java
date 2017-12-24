package org.neusoft.neubbs.constant.api;

/**
 * 设置信息
 *      - 项目使用各项常量设置
 *
 * @author Suvan
 */
public final class SetConst {

    private SetConst() { }

    /**
     * 图片验证码（储存在 Session）
     */
    public static final String SESSION_CAPTCHA = "captcha";

    /**
     * token 验证（权限）
     */
    public static final String RANK_ADMIN = "admin";


    /**
     * 过期时间（时间戳，ms）
     *      - 1天
     */
    public static final long EXPIRE_TIME_MS_ONE_DAY = 86400000L;
    public static final long EXPIRE_TIME_MS_SIXTY_SECOND = 60000L;
    public static final int EXPIRETIME_S_ZERO  = 0;
    public static final long REDIS_EXPIRED = -2L;

    /**
     * 天
     */
    public static final long ONE_DAY_MS = 86400000L;
    public static final String TODAY = "今天";
    public static final String DAY_AGE = " 天前";

    /**
     * 临时密码长度
     */
    public static final int FORGET_PASSWORD_RANDOM_LENGTH = 6;

    /**
     * 数字
     */
    public static final int NEGATIVE_ONE = -1;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THOUSAND = 1000;
    public static final int TWENTY_FOUR = 24;
    public static final int SIXTY = 60;

    /**
     * 长度
     */
    public static final int LENGTH_TWO = 2;

    /**
     * 面积
     */
    public static final int SIZE_ONE = 1;
    public static final int SIZE_TWO = 2;
    public static final int SIZE_THREE = 3;
    public static final int SIZE_FOUR = 4;

    /**
     * 用户头像
     *      - 1 KB = 1024 byte
     *      - 1 MB = 1024 KB
     *      - 1 GB = 1024 MB
     *      - 1 TB = 1024 GB
     */
    public static final long SIZE_ONE_MB = 1048576;
    public static final long SIZE_FIVE_MB = 5242880;

    /**
     * 账户激活状态
     */
    public static final int ACCOUNT_STATE_TRUE = 1;
    public static final int ACCOUNT_STATE_FALSE = 0;

    /**
     * Response 响应信息格式
     */
    public static final String CHARACTER_ENCODING = "utf-8";
    public static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * 默认字符串
     *      - 用于区分用户头像是否为默认值
     */
    public static final String DEFAULT = "default";

    /**
     * 发送邮箱
     */
    public static final String EMAIL_SENDER_NAME = "Neubbs";
    public static final String EMAIL_SUBJECT_ACTIVATE = "Neubbs 账户激活";
    public static final String EMAIL_SUBJECT_TEMPORARY_PASSWORD = "Neubbs 账户临时密码";
    public static final String EMAIL_TIMER = "timer";

    /**
     * Redis 数据库
     */
    public static final String KEY_ACTIVATE = "activate";

    /**
     * TCP/IP 协议
     */
    public static final String HTTP = "http";
    public static final String FTP = "ftp";

    /**
     * 指令
     *     - 自增 1
     *     - 自减 1
     *     - 0-false
     *     - 1-true
     */
    public static final String INC = "inc";
    public static final String DEC = "dec";
    public static final String ZERO_S = "0";
    public static final String ONE_S = "1";

    /**
     * 用户行为
     *      - 收藏 +1
     *      - 收藏 -1
     */
    public static final String COLLECT_INC = "collect inc";
    public static final String COLLECT_DEC = "collect dec";

    /**
     * 话题行为
     *      - 被收藏用户 +1
     *      - 被收藏用户 -1
     */
    public static final String COLLECT_USER_INC = "collect user inc";
    public static final String COLLECT_USER_DEC = "collect user dec";

}
