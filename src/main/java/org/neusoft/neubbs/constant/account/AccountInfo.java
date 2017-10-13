package org.neusoft.neubbs.constant.account;

/**
 * 用户信息
 */
public interface AccountInfo {
    String ID = "id";
    String USERNAME = "username";
    String NAME = "name";
    String PASSWORD = "password";
    String SEX = "sex";
    String BIRTHDAY = "birthday";
    String PHONE = "phone";
    String EMAIL = "email";
    String ADDRESS = "address";
    String CREATETIME = "createtime";

    //空判断
    String PARAM_USERNAME_NO_NULL = "参数 username，不能为空";
    String PARAM_PASSWORD_NO_NULL = "参数 password，不能为空";
    String PARAM_EMAIL_NO_NULL = "参数 email，不能为空";
    String PARAM_RANK_NO_NULL = "参数 rank，不能为空";

    //数据库判断
    String DATABASE_NO_EXIST_USER = "数据库中不存在该用户";
    String DATABASE_ALREAD_EXIST_USER_NO_AGAIN_ADD = "数据库已经存在用户，请勿再次添加";

    //错误
    String USER_PASSWORD_NO_MATCH = "用户密码不匹配";

    //格式错误
    String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
    String ID_STYLE_ERROR_NO_PPURENUMBER = "id 格式错误，存在非数字字符，请重新输入";

    //成功
    String GET_USER_INFORMATION_SUCCESS = "获取用户信息成功！";
    String USER_PASS_AUTHENTICATE_LOGIN_SUCCESS = "用户通过验证，登录成功，将在7天内自动登录!";
    String USER_LOGOU_SUCCESS = "用户注销成功!";
    String REGISTER_USER_SUCCESS = "注册用户成功！";
    String UPDATE_USER_PASSWORD_SUCCESS = "更新用户密码成功!";
    String UPDATE_USER_RANK_SUCCESS = "更新用户权限成功!";

    //权限问题
    String NO_VISIT_AHTORITY_PLEASE_LOGIN = "无访问权限，请登录后执行操作！";
    String USER_RANK_NO_ENOUGH = "用户权限不足，非管理员用户无法调用此 api";

    //发送邮箱验证码
    String EMAILCODE = "emailcode";
    String EMAILCODE_HOST = "smtp.qq.com";
    String EMAILCODE_FROM_USERNAME = "526097449@qq.com";
    String EMAILCODE_FROM_PASSWORD = "pxjkgvpuvtngbhbe";
    String EMAILCODE_SMTP = "smtp";
    String EMAILCODE_AUTH = "mail.smtp.auth";
    String EMAILCODE_AUTH_TRUE = "true";
    String EMAILCODE_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
    String EMAILCODE_JAVAX_NET_SSL_SSLSOCKETFACTORY = "javax.net.ssl.SSLSocketFactory";
    String EMAILCODE_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";
    String EMAILCODE_SMTP_SSL_PROT = "465";
    String EMAILCODE_TO_SUBJECT = "neubbs 邮箱验证码";
    String EMAILCODE_TO_TEXT = "您好，您的邮箱验证码为: ";
    String EMAILCODE_SUCCESS = "邮箱验证码,已成功发送到指定用户邮箱";
    String EMAILCODE_FAIL = "获取邮箱验证码失败";
    String EMAILCODE_EMAIL_NUNULL = "获取邮箱验证码，邮箱不能为空";
}
