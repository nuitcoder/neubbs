package org.neusoft.neubbs.constant.api;

/**
 * Api 警告信息
 *
 * @author Suvan
 */
public final class ApiMessage {

    private ApiMessage() { }

    public static final String PARAM_ERROR  = "incorrect input parameter";
    public static final String DATABASE_EXCEPTION = "database exception";

    /**
     * Account api 警告信息
     */
    public static final String NO_USER = "the user does not exist";
    public static final String USERNAME_REGISTERED = "username has been registered";
    public static final String EMAIL_REGISTERED = "the email has bean registered";
    public static final String NO_ACTIVATE   = "the account has not been activated";
    public static final String ACCOUNT_ACTIVATED = "the account has bean activated";
    public static final String ACTIVATION_SUCCESSFUL =  "activate success";

    public static final String USERNAME_OR_PASSWORD_INCORRECT = "username or password is incorrect";
    public static final String CAPTCHA_INCORRECT = "input captcha incorrect";

    public static final String NO_PERMISSION = "no permission";
    public static final String LINK_INVALID = "invalid activation link";
    public static final String TOKEN_EXPIRED = "token expired";
    public static final String NO_GENERATE_CAPTCHA = "no generate captcha";

    public static final String MAIL_SEND_SUCCESS = "mail send success";
    public static final String WATI_TIMER = "wait for timer";

    public static final String IVALID_TOKEN = "invalid token";

    public static final String GENERATE_CAPTCHA_FAIL = "generate captcha fail";

    /**
     * Count api 警告信息
     */

    /**
     * File api 警告信息
     */
    public static final String NO_CHOICE_PICTURE = "no choice to upload picture";
    public static final String PICTURE_FORMAT_WRONG = "user upload picture type is wrong,"
            + " only *.jpg or *.png or *.jpeg";
    public static final String NO_PARENT_DIRECTORY = "there is no parent directory";
    public static final String UPLOAD_FAIL = "upload fail";
    public static final String UPLOAD_SUCCESS = "upload success";

    /**
     * Topic api 警告信息
     */
    public static final String NO_TOPIC = "no topic";
    public static final String NO_REPLY = "no reply";

    public static final String FAIL_GET_TOPIC_LSIT = "failed to get topic list";

}
