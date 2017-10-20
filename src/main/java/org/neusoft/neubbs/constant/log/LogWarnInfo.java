package org.neusoft.neubbs.constant.log;

/**
 * 日志警告信息
 *
 * @author Suvan
 */
public interface LogWarnInfo {

    /*****************************Account api**********************************/
    String DATABASE_NO_EXIST_USER = " 数据库中不存在该用户!";
    String DATABASE_ALREAD_EXIST_USER_NO_AGAIN_ADD = " 数据库已经存该用户，请勿再次添加";

    String ACCOUNT_NO_ACTIVATION_NO_LOGIN = " 账户未激活，无法登录，请到指定邮箱，点击激活链接，激活账户";
    String ACCOUNT_ACTIVATION_URL_ALREAD_EXPIRE_TIME = " 该激活链接已经过期，请重新注册";
    String ACCOUNT_ACTIVATION_FAIL_EMAIL_NO_REGISTER = "账户激活失败，该邮箱未注册，";
    String ACCOUNT_ACTIVATION_SUCCESSFUL = " 邮箱账户激活成功！";

    String USER_PASSWORD_NO_MATCH = " 用户密码不匹配 ，请重新输入";


    /*****************************Email api**********************************/
    String EMAIL_NO_REGISTER_NO_SEND_EMAIL = "该邮箱未注册，无法发送邮件";
    String ACTIVATION_EMAIL_SEND_SUCCESS = "激活邮件发送成功！";


    /*****************************Topic api**********************************/
    String SAVE_TOPIC_ID_NONULL = "保存主题，id 不能为空";
    String SAVE_TOPIC_TITLE_NONULL = "保存主题，title 不能为空";
    String SAVE_TOPIC_CATEGORY_NONULL = "保存主题，category 不能为空";
    String SAVE_TOPIC_SUCCESS = "保存主题成功！";
    String SAVE_TOPIC_FAIL = "保存主题失败！";


    /*****************************File api**********************************/
    String USER_NO_CHOICE_UPLOAD_FILE = "用户没有选择上传文件!";
    String FILE_TYPE_NO_USER_IMAGE_SPECIFY_TYPE = "文件类型不符合用户头像指定类型，请重新选择上传";
    String USER_IMAGE_SAVE_DATABASE_FAIL = "用户头像，保存数据库失败，删除已上传图片";
    String DELETE_IMAGE_FILE_FAIL = "删除图片文件失败,请手动处理仍然停留在服务器的文件！";


    /*****************************Api Token Interceptor**********************************/
    String JWT_TOKEN_ALREAD_EXPIRE = "JWT 的 token 已经过期，解密失败";
    String NO_VISIT_AHTORITY_PLEASE_LOGIN = "无访问权限，请登录后执行操作！";
    String USER_RANK_NO_ENOUGH_NO_ADMIN = "用户权限不足，非管理员用户无法调用此 api";

}
