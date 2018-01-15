package org.neusoft.neubbs.constant.api;

/**
 * Api 警告信息
 *
 * @author Suvan
 */
public final class ApiMessage {

    private ApiMessage() { }

    /**
     * 用户
     */
    public static final String NO_USER = "the user does not exist";
    public static final String USER_OPERATE_FAIL = "user operate fail";
    public static final String USERNAME_REGISTERED = "username has been registered";
    public static final String EMAIL_REGISTERED = "the email has bean registered";
    public static final String NO_ACTIVATE   = "the account has not been activated";
    public static final String ACCOUNT_ACTIVATED = "the account has bean activated";
    public static final String USERNAME_OR_PASSWORD_INCORRECT = "username or password is incorrect";

    /**
     * 验证码
     */
    public static final String NO_GENERATE_CAPTCHA = "no generate captcha";
    public static final String GENERATE_CAPTCHA_FAIL = "generate captcha fail";
    public static final String CAPTCHA_INCORRECT = "input captcha incorrect";

    /**
     * token
     */
    public static final String INVALID_TOKEN = "invalid token";
    public static final String TOKEN_EXPIRED = "token expired";

    /**
     * 错误
     */
    public static final String UNKNOWN_ERROR = "unknown error";

    /**
     * 计时器
     */
    public static final String WAIT_TIMER = "wait for timer";

    /**
     * 请求参数
     */
    public static final String PARAM_ERROR  = "incorrect input parameter";
    public static final String LINK_INVALID = "invalid activation link";

    /**
     * 请求参数
     */
    public static final String NO_PERMISSION = "no permission";

    /**
     * 数据库
     */
    public static final String DATABASE_EXCEPTION = "database exception";

    /**
     * 上传文件
     */
    public static final String NO_CHOICE_PICTURE = "no choice to upload picture";
    public static final String PICTURE_FORMAT_WRONG = "user upload picture type is wrong,"
            + " only *.jpg or *.png or *.jpeg";
    public static final String NO_PARENT_DIRECTORY = "there is no parent directory";
    public static final String PICTURE_TOO_LARGE = "the picture too large";
    public static final String UPLOAD_FAIL = "upload fail";
    public static final String UPLOAD_SUCCESS = "upload success";

    /**
     * 话题
     */
    public static final String NO_TOPIC = "no topic";
    public static final String NO_REPEAT_INC_TOPIC_LIKE = "no repeat 'inc' topic like";
    public static final String NO_REPEAT_DEC_TOPIC_LIKE = "no repeat 'dec' topic like";
    public static final String NO_QUERY_TOPICS = "no query topics";
    public static final String USER_NO_LIKE_THIS_TOPIC = "user no like this like";
    public static final String USER_OPERATE_TOPIC_FAIL = "user operate topic fail";
    public static final String TOPIC_RECORD_OPERATE_FAIL = "topic record operate fail";
    public static final String QUERY_EXCEED_TOPIC_NUMBER = "query exceed max topic number";

    /**
     * 话题回复
     */
    public static final String NO_REPLY = "no reply";

    /**
     * 话题
     */
    public static final String NO_CATEGORY = "no category";
    public static final String ALREAD_EXIST_CATEGORY_NICK = "already exist category id, no repeat save";
    public static final String ALREAD_EXIST_CATEGORY_NAME = "already exist category name, no repeat save";

    /**
     * ftp 服务
     */
    public static final String FTP_SERVICE_EXCEPTION = "ftp service exception";

}
