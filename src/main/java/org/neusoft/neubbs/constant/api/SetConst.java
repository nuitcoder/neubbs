package org.neusoft.neubbs.constant.api;

/**
 * 设置常量
 *      - 时间
 *      - 密码与权限
 *      - HTTP
 *      - 数字类型
 *      - 判断类型
 *      - 邮箱服务
 *      - 图片
 *      - 数据库
 *      - 指令
 *      - 记录行为
 *
 * @author Suvan
 */
public final class SetConst {

    private SetConst() { }

    /*
     * ***********************************************
     *  时间
     * ***********************************************
     */

    /**
     * 时间
     *      1000 ms
     *      24 小时
     *      60 秒
     *      60 分钟
     */
    public static final int TIME_THOUSAND_MS = 1000;
    public static final int TIME_TWENTY_FOUR_HOUR = 24;
    public static final int TIME_SIXTY_S = 60;
    public static final int TIME_SIXTY_MIN = 60;

    /**
     * 日期时间
     *      - 1 天（ms，毫秒）
     */
    public static final long ONE_DAY_MS = 86400000L;

    /**
     * 过期时间
     *      - 60 秒 (ms)
     *      - 0 s
     *      - Redis 不存在 key ，值为 -2
     */
    public static final long EXPIRE_TIME_SIXTY_SECOND_MS = 60000L;
    public static final int EXPIRE_TIME_ZERO_S  = 0;
    public static final long EXPIRE_TIME_REDIS_NO_EXIST_KEY = -2L;


    /*
     * ***********************************************
     * 密码与权限
     * ***********************************************
     */

    /**
     * 临时密码长度
     */
    public static final int TEMPORARY_PASSWORD_LENGTH = 6;

    /**
     * JWT Token 密钥
     */
    public static final String JWT_TOKEN_SECRET_KEY = "this neubbs is best";

    /**
     * 权限
     *      - 管理员权限
     */
    public static final String RANK_ADMIN = "admin";


    /*
     * ***********************************************
     * HTTP
     * ***********************************************
     */

    /**
     * Session 储存
     *      - 图片验证码（储存在 Session）
     */
    public static final String SESSION_CAPTCHA = "captcha";


    /*
     * ***********************************************
     * 数字类型
     * ***********************************************
     */

    /**
     * 性别
     */
    public static final int SEX_NO = -1;
    public static final int SEX_GIRL = 0;
    public static final int SEX_BOY = 1;

    /**
     * 进制
     *      - 16 进制
     */
    public static final int REDIX_SIXTEEN = 16;

    /**
     * 索引
     *      - 不存在字符（索引位置返回 -1）
     */
    public static final int INDEX_NO = -1;

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
    public static final int SIZE_SEVEN = 7;

    /**
     * 字符的 ASCII 码数字范围
     *      - 小写字母 ASCII 最小值，最大值
     *      - 大写字母 ASCII 最小值，最大值
     *      - 单个数字最小值，最大值
     */
    public static final int LOWERCASE_ASCII_MIN = 97;
    public static final int LOWERCASE_ASCII_MAX = 122;
    public static final int UPPERCASE_ASCII_MIN = 65;
    public static final int UPPERCASE_ASCII_MAX = 90;
    public static final int FIGURE_MIN = 0;
    public static final int FIGURE_MAX = 10;

    /**
     * 字母
     *      - 所有字母数量（26 个字母）
     */
    public static final int ALL_LETTER_AMOUNT = 26;

    /**
     * 指针
     *      - 适用于流程判断
     */
    public static final int POINT_ONE = 1;
    public static final int POINT_TWO = 2;
    public static final int POINT_THREE = 3;

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
     *      - 用户未激活，0 表示
     */
    public static final int ACCOUNT_ACTIVATED_STATE = 1;
    public static final int ACCOUNT_NO_ACTIVATED_STATE = 0;


    /*
     * ***********************************************
     * 判断类型
     * ***********************************************
     */

    public static final String VALUE_NULL = "null";

    /**
     * hashCode 值（用于在重写 hashCode() 参与递进式运算）
     *      - UserDo 的常数值
     */
    public static final int USERDO_HASH_CONSTANT = 89;


    /*
     * ***********************************************
     * 邮箱服务
     * ***********************************************
     */

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
     * 邮件格式
     */
    public static final String FROM_SUBJECT_ENCODING = "UTF-8";
    public static final String FROM_CONTENT_TYPE = "text/html;charset=UTF-8";

    /**
     * 腾讯企业邮箱
     */
    public static final String TO_HOST = "smtp.exmail.qq.com";
    public static final String TO_SMTP = "smtp";
    public static final String TO_AUTH = "mail.smtp.auth";
    public static final String TO_AUTH_TRUE = "true";
    public static final String TO_MAIL_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
    public static final String TO_JAVAX_NET_SSL_SSLSOCKETFACTORY = "javax.net.ssl.SSLSocketFactory";
    public static final String TO_MAIL_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";
    public static final String TO_SMTP_SSL_PROT = "465";


    /*
     * ***********************************************
     * 图片
     * ***********************************************
     */

    public static final String USER_DEFAULT_AVATAR = "default-avator-min.jpeg";

    /*
     * ***********************************************
     * 数据库
     * ***********************************************
     */

    /**
     * 数据源类型
     *      - 腾讯云 MySQL
     *      - 本地 MySQL
     */
    public static final String CLOUD_DATA_SOURCE_MYSQL = "cloudDataSourceMysql";
    public static final String LOCALHOST_DATA_SOURCE_MYSQL = "localDataSourceMysql";

    /**
     * Redis 数据库储存
     */
    public static final String VALUE_MAIL_SEMD_INTERVAL = "mail send interval";


    /*
     * ***********************************************
     * 指令
     * ***********************************************
     */

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


    /*
     * ***********************************************
     * 记录行为
     * ***********************************************
     */

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
