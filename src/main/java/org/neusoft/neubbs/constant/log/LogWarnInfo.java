package org.neusoft.neubbs.constant.log;

/**
 * 日志警告信息
 */
public interface LogWarnInfo {

    /*****************************Account api**********************************/
    //数据库判断
    String DATABASE_NO_EXIST_USER = " 数据库中不存在该用户!";
    String DATABASE_ALREAD_EXIST_USER_NO_AGAIN_ADD = " 数据库已经存该用户，请勿再次添加";

    //帐号激活
    String ACCOUNT_NO_ACTIVATION_NO_LOGIN = " 账户未激活，无法登录，请到指定邮箱，点击激活链接，激活账户";
    String ACCOUNT_ACTIVATION_URL_ALREAD_EXPIRE_TIME = " 该激活链接已经过期，请重新注册";
    String ACCOUNT_ACTIVATION_FAIL_EMAIL_NO_REGISTER = "账户激活失败，该邮箱未注册，";

    //密码匹配
    String USER_PASSWORD_NO_MATCH = " 用户密码不匹配 ，请重新输入";

    /*****************************Email api**********************************/
    String EMAIL_NO_REGISTER_NO_SEND_EMAIL = "该邮箱未注册，无法发送邮件";
    String ACTIVATION_EMAIL_SEND_SUCCESS = "激活邮件发送成功！";

    /*****************************Api Token Interceptor**********************************/
    String JWT_TOKEN_ALREAD_EXPIRE = "JWT 的 token 已经过期，解密失败";
    String NO_VISIT_AHTORITY_PLEASE_LOGIN = "无访问权限，请登录后执行操作！";
    String USER_RANK_NO_ENOUGH_NO_ADMIN = "用户权限不足，非管理员用户无法调用此 api";

}
