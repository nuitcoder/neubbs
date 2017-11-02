package org.neusoft.neubbs.constant.api;

/**
 * 设置信息
 *
 * @author Suvan
 */
public final class SetConst {

    private SetConst() { }

    /**
     * 激活邮件地址
     */
    public static final String MAIL_ACCOUNT_ACTIVATION_URL = "http://localhost:8080/api/account/validate?token=";

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
     */
    public static final long EXPIRE_TIME_MS_ONE_DAY = 86400000L;
    public static final long EXPIRE_TIME_MS_SERVEN_DAY = 604800000L;
    public static final int EXPIRE_TIME_S_ONE_DAY = 1080000;
    public static final int EXPIRETIME_S_ZERO  = 0;

    /**
     * 临时密码长度
     */
    public static final int FORGET_PASSWORD_RANDOM_LENGTH = 6;

    /**
     * 长度
     */
    public static final int LENGTH_TWO = 2;

    /**
     * 面积
     */
    public static final int SIZE_TWO = 2;
    public static final int SIZE_THREE = 3;
    public static final int SIZE_FOUR = 4;

    /**
     * 数字
     */
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int TWENTY_SIX = 26;
    public static final int SIXTY_FIVE = 65;
    public static final int NINETH_SEVEN = 97;
    public static final int TEN_THOUSAND = 10000;

    /**
     * 用户头像
     */
    public static final String UPLOAD_USER_IMAGE_PATH = "/WEB-INF/file/user/image/";
    public static final long SIZE_ONE_MB = 1048576;

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

}
