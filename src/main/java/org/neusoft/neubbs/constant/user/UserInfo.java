package org.neusoft.neubbs.constant.user;

/**
 * 用户信息
 */
public interface UserInfo {
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

    String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";

    //操作成与失败
    String REGISTER_SUCCESS = "用户注册成功";
    String REGISTER_FAIL = "用户注册失败";
    String REMOVE_SUCCESS = "用户删除成功";
    String REMOVE_FAIL = "用户删除失败";
    String GET_USERINFO_SUCCESS = "获取用户信息成功";
    String GET_USERINFO_FAIL = "获取用户信息失败";
    String GET_ALL_ADMININFO_SUCCESS = "获取所有管理员信息成功";
    String GET_ALL_ADMININFO_FAIL = "获取所有管理员信息失败";
    String GET_ASSIGIN_DATE_REGISTER_USERINFO_SUCCESS = "获取指定日期注册的用户信息成功";
    String GET_ASSIGIN_DATE_REGISTER_USERINFO_FAIL = "获取指定日期注册的用户信息失败";
    String GET_ALL_USERINFO_SUCCESS = "获取所有用户信息，成功";
    String GET_ALL_USERINFO_FAIL = "获取所有用户信息，失败";
    String ALTER_PASSWORD_SUCCESS = "用户修改密码成功";
    String ALTER_PASSWORD_FAIL = "用户修改密码失败";
    String ALTER_RANK_SUCCESS = "用户修改权限成功";
    String ALTER_RANK_FAIL = "用户修改权限失败";

    //用户名唯一
    String DATABASE_ALREAD_EXIST_USER = "数据库已经存在该用户，请修改用户名";
    String ID_STYLE_ERROR_NO_PPURENUMBER = "id 格式错误，存在非数字字符，请重新输入";

    //数据库不存在
    String DATABASE_NO_EXIST_USER = "数据库中不存在该用户";
    String NO_EXIST_ADMINUSER = "不存在管理员用户";
    String ASSIGIN_DATE_NO_EXIST_REGISTER_USER = "指定日期无注册用户";

    //空判断
    String GET_USERINFO_USERNAME_NONULL = "获取用户信息，用户名不能为空";
    String GET_USERINFO_ID_NONULL = "获取用户信息，id 不能为空";
    String GET_USERINFO_YEAR_NONULL = "获取用户信息，year 不能为空";
    String GET_USERINFO_MONTH_NONULL = "获取用户信息，month 不能为空";
    String GET_USERINFO_YEAR_STYLE_ERROR = "year 格式错误，请重新输入";
    String GET_USERINFO_MONTH_STYLE_ERROR = "month 格式错误，请重新输入";
    String REGISTER_USERNAME_NONULL = "注册用户，username 不能为空";
    String REGISTER_PASSWORD_NONULL = "注册用户，password 不能为空";
    String REGISTER_EMAIL_NONULL = "注册用户，eamil 不能为空";
    String REMOVE_ID_NONULL = "删除用户，id 不能为空";
    String UPDATE_PASSWORD_ID_NONULL = "修改密码，id 不能为空";
    String UPDATE_PASSWORD_PASSWORD_NONULL = "修改密码， passwword 不能为空";
    String UPDATE_RANK_ID_NONULL = "修改权限，id 不能为空";
    String UPDATE_RANK_RANK_NONULL = "修改权限，rank 不能为空";

    //权限问题
    String RANK_NO_ENOUGH = "权限不足，无法调用 api";
}
