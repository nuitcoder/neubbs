package org.neusoft.neubbs.constant.ajax;

/**
 * 请求参数信息
 */
public interface RequestParamInfo {
    //空判断
    String PARAM_USERNAME_NO_NULL = "参数 username，不能为空";
    String PARAM_PASSWORD_NO_NULL = "参数 password，不能为空";
    String PARAM_EMAIL_NO_NULL = "参数 email，不能为空";
    String PARAM_TOKEN_NO_NULL = "参数 token，不能为空";

    //用户名判断
    String PARAM_USERNAME_LENGTH_NO_MATCH_SCOPE = "参数 username ，长度不符合范围（ 3 <= length <= 15）";
    String PARAM_USERNAME_STYLE_NO_MEET_NORM = "参数 username ，格式不符合规范（A-Z a-z 0-9）";

    //密码判断
    String PARAM_PASSWORD_LENGTH_NO_MATCH_SCOPE = "参数 password，长度不符合范围（3 <= length <= 16）";

    //邮箱判断
    String PARAM_EMAIL_STYLE_NO_MEET_NORM = "参数 email，邮箱格式不符合规范，请重新输入！";

}
