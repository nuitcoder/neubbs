package org.neusoft.neubbs.constant;

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

    //格式
    String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
    String ID_STYLE_ERROR_NO_PPURENUMBER = "id 格式错误，存在非数字字符，请重新输入";

    //数据库反馈
    String DATABASE_NO_EXIST_USER = "数据库中不存在该用户";
    String DATABASE_ALREAD_EXIST_USER = "数据库已经存在用户，请勿重复添加";

    //权限问题
    String RANK_NO_ENOUGH = "权限不足，无法调用 api";

    //获取用户信息
    String GET_USERINFO_SUCCESS = "获取用户信息成功";
    String GET_USERINFO_FAIL = "获取用户信息失败";
    String GET_USERINFO_USERNAME_NONULL = "获取用户信息，username 不能为空";

    //注册用户
    String REGISTER_USER_SUCCESS = "注册用户成功！";
    String REGISTER_USER_FAIL = "注册用户失败";
    String REGISTER_USER_USERNAME_NONULL = "注册用户，username 不能为空";
    String REGISTER_USER_PASSWORD_NONULL = "注册用户，password 不能为空";
    String REGISTER_USER_EMAIL_NONULL = "注册用户， email 不能为空";

    //修改用户密码
    String UPDATE_USER_PASSWORD_SUCCESS = "更新用户密码成功";
    String UPDATE_USER_PASSWORD_FAIL = "更新用户密码失败";
    String UPDATE_USER_PASSWORD_USERNAME_NONULL = "更新用户密码，username 不能为空";
    String UPDATE_USER_PASSWORD_PASSWORD_NONULL = "更新用户密码，password 不能为空";

    //修改用户权限
    String UPDATE_USER_RANK_SUCCESS = "更新用户权限成功";
    String UPDATE_USER_RANK_FAIL = "更新用户权限失败";
    String UPDATE_USER_RANK_USERNAME_NONULL = "更新用户权限，username 不能为空";
    String UPDATE_USER_RANK_RANK_NONULL = "更新用户权限，rank 不能为空";
}
