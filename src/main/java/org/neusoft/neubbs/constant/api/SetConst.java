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
     * Session 储存
     *      - 图片验证码（储存在 Session）
     */
    public static final String SESSION_CAPTCHA = "captcha";

    /**
     * 权限
     *      - 管理员权限
     */
    public static final String RANK_ADMIN = "admin";

    /**
     * 过期时间
     *      - 60 秒 (ms)
     *      - 0 s
     *      - Redis 不存在 key ，值为 -2
     */
    public static final long EXPIRE_TIME_SIXTY_SECOND_MS = 60000L;
    public static final int EXPIRE_TIME_ZERO_S  = 0;
    public static final long EXPIRE_TIME_REDIS_NO_EXIST_KEY = -2L;

    /**
     * 日期时间
     *      - 1 天（ms，毫秒）
     */
    public static final long ONE_DAY_MS = 86400000L;

    /**
     * 临时密码长度
     */
    public static final int TEMPORARY_PASSWORD_LENGTH = 6;

    /**
     * JWT Token 密钥
     */
    public static final String JWT_TOKEN_SECRET_KEY = "this neubbs is best";

    /**
     * 数据源类型
     *      - 腾讯云 MySQL
     *      - 本地 MySQL
     */
    public static final String CLOUD_DATA_SOURCE_MYSQL = "cloudDataSourceMysql";
    public static final String LOCALHOST_DATA_SOURCE_MYSQL = "localDataSourceMysql";

    /**
     * 数字
     */
    public static final int NEGATIVE_ONE = -1;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int FIVE = 5;
    public static final int THOUSAND = 1000;
    public static final int TWENTY_FOUR = 24;
    public static final int SIXTY = 60;

    /**
     * 长度
     */
    public static final int LENGTH_TWO = 2;

    /**
     * 面积
     *   - 初始化集合对象时，声明初始长度
     */
    public static final int SIZE_ONE = 1;
    public static final int SIZE_TWO = 2;
    public static final int SIZE_THREE = 3;

    /**
     * 用户头像最大面积
     *      - 1 KB = 1024 byte
     *      - 1 MB = 1024 KB
     *      - 1 GB = 1024 MB
     *      - 1 TB = 1024 GB
     */
    public static final long USER_AVATOR_MAX_SIZE_FIVE_MB = 5242880;

    /**
     * 账户激活状态
     *      - 用户已激活，1 表示
     */
    public static final int ACCOUNT_ACTIVATED_ONE = 1;

    /**
     * 邮箱服务
     *      - 邮件发送人
     *      - 邮件主题（账户激活）
     *      - 邮件主题（账户临时密码）
     */
    public static final String EMAIL_SENDER_NAME = "Neubbs";
    public static final String EMAIL_SUBJECT_ACTIVATE = "Neubbs 账户激活";
    public static final String EMAIL_SUBJECT_TEMPORARY_PASSWORD = "Neubbs 账户临时密码";

    /**
     * Redis 数据库储存
     */
    public static final String VALUE_MAIL_SEMD_INTERVAL = "mail send interval";

    /**
     * 指令
     *     - 自增 1
     *     - 自减 1
     *     - 0
     *     - 1
     */
    public static final String COMMAND_INC = "inc";
    public static final String COMMAND_DEC = "dec";
    public static final String COMMAND_ZERO = "0";
    public static final String COMMAND_ONE = "1";

    /**
     * 用户行为
     *      - 喜欢话题 +1
     *      - 喜欢话题 -1
     *      - 收藏话题 +1
     *      - 收藏话题 -1
     *      - 关注话题 +1
     *      - 关注话题 -1
     *      - 主动关注用户 +1
     *      - 主动关注用户 -1
     *      - 被关注用户 +1
     *      - 被关注用户 -1
     */
    public static final String LIKE_INC = "like inc";
    public static final String LIKE_DEC = "like dec";
    public static final String COLLECT_INC = "collect inc";
    public static final String COLLECT_DEC = "collect dec";
    public static final String ATTENTION_INC = "attention inc";
    public static final String ATTENTION_DEC = "attention dec";
    public static final String FOLLOWING_INC = "following inc";
    public static final String FOLLOWING_DEC = "following dec";
    public static final String FOLLOWED_INC = "followed inc";
    public static final String FOLLOWED_DEC = "followed dec";

    /**
     * 话题行为
     *      - 被喜欢用户 +1
     *      - 被喜欢用户 -1
     *      - 被收藏用户 +1
     *      - 被收藏用户 -1
     *      - 被关注用户 +1
     *      - 被关注用户 -1
     *
     */
    public static final String LIKE_USER_INC = "like user inc";
    public static final String LIKE_USER_DEC = "like user dec";
    public static final String COLLECT_USER_INC = "collect user inc";
    public static final String COLLECT_USER_DEC = "collect user dec";
    public static final String ATTENTION_USER_INC = "attention user inc";
    public static final String ATTENTION_USER_DEC = "attention user dec";
}
