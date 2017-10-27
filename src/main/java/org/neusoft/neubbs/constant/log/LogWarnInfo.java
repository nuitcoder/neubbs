package org.neusoft.neubbs.constant.log;

/**
 * 日志警告信息
 *
 * @author Suvan
 */
public interface LogWarnInfo {
    /*****************************Account api**********************************/
    String DATABASE_NO_EXIST_USER = " 数据库中不存在该用户 ";
    String DATABASE_ALREAD_EXIST_USER = " 数据库已经存该用户 ";

    String NO_ACTIVATION_NO_PERMISSION_USE_API = "账户未激活，无权调用 api";
    String ACTIVATION_URL_ALREAD_EXPIRE_TIME = " 激活口令已经过期";
    String AUTHENTICATION_TOKEN_ALREAD_EXPIRE_TIME = "授权口令已经过期";
    String EMAIL_NO_REGISTER_NO_SEND_EMAIL = " 邮箱未注册，无法发送邮件";
    String EMAIL_ACTIVATED_NO_AGAIN_SEND_EMAIL = " 邮箱用户已经激活，不重复发送邮件";
    String EMAIL_OCCUPIED = " 邮箱已被占用";

    String USER_PASSWORD_INCORRECT = " 用户密码不正确";
    String NO_GENERATE_CAPTCHA_NO_VERIFY = "未生成验证码，无法验证";
    String CAPTCHA_INCORRECT = " 验证码不正确";

    String NO_PERMISSION_UPDATE_OTHER_USER = "无权修改其余用户";

    String MISSING_USERNAME_OR_EMAIL_PARAM_NO_GET_ACCOUNT_INFO = "找不到 username 或 email 参数（选其一即可），无法获取账户信息";


    /*****************************Topic api**********************************/
    String NO_EXIST_TOPIC = "不存在指定主题，请检查输入（主题 id）";
    String NO_EXIST_REPLY = "不存在指定回复，请检查输入（回复 id）";


    /*****************************File api**********************************/
    String USER_NO_CHOICE_UPLOAD_FILE = "用户没有选择上传文件 ";
    String FILE_TYPE_NO_MATCH_IMAGE_TYPE = " 文件类型不符合头像类型（jpg | png | jpeg）";
    String SERVER_NO_EXIST_UPLOAD_SAVE_DIRECTORY = "服务器不存在指定上传文件目录";


    /*****************************Api Token Interceptor**********************************/
    String TOKEN_ALREAD_EXPIRE = "token 已经过期，需重新登录";
    String NO_PERMISSION_NEED_LOGIN = "无权访问 api，需登录";
    String USER_RANK_NO_ENOUGH_NO_ADMIN = "用户权限不足，非管理员用户无法调用此 api";


    /******************************User Service*********************************/
    String USER_SAVE_FAIL = "用户保存失败";
    String USER_ALTER_FAIL = "用户修改失败";


    /******************************Topic Service*********************************/
    String TOPIC_SAVE_FAIL = "主题保存失败";
    String TOPIC_CONTENT_SAVE_FAIL = "主题内容保存失败";
    String TOPIC_REPLY_SAVE_FAIL = "主题回复保存失败";
    String TOPIC_REMOVE_FAIL = "主题删除失败";
    String TOPIC_CONTENT_REMOVE_FAIL = "主题内容删除失败";
    String TOPIC_REPLY_REMOVE_FAIL = "主题回复删除失败";
    String TOPIC_ALTER_FAIL = "主题修改失败";
    String TOPIC_CONTENT_ALTER_FAIL = "主题内容修改失败";
    String TOPIC_REPLY_ALTER_FAIL = "主题回复修改失败";


    /******************************Redis Service*********************************/
}
