package org.neusoft.neubbs.util;

import org.neusoft.neubbs.constant.ajax.RequestParamInfo;

/**
 * Request 请求参数检查 工具类
 */
public class RequestParamsCheckUtils {

    /**
     * 检查用户名（通过检查返回null，未通过返回错误信息）
     * @param username
     * @return String
     */
    public static String username(String username){
        //非空检查
        if(StringUtils.isEmpty(username)){
            return RequestParamInfo.PARAM_USERNAME_NO_NULL;
        }

        //长度检查
        if(!StringUtils.isScope(username, 3, 15)){
            return RequestParamInfo.PARAM_USERNAME_LENGTH_NO_MATCH_SCOPE;
        }

        //正则检查
        if(!PatternUtils.matchUsername(username)){
            return RequestParamInfo.PARAM_USERNAME_STYLE_NO_MEET_NORM;
        }

        return null;
    }

    /**
     * 检查密码
     * @param password
     * @return String
     */
    public static String password(String password){
        if (StringUtils.isEmpty(password)) {
            return RequestParamInfo.PARAM_PASSWORD_NO_NULL;
        }

        if(!StringUtils.isScope(password, 6, 16)){
            return RequestParamInfo.PARAM_PASSWORD_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 检查邮箱
     * @param email
     * @return String
     */
    public static String email(String email){
        if(StringUtils.isEmpty(email)){
            return RequestParamInfo.PARAM_EMAIL_NO_NULL;
        }

        if (!PatternUtils.matchEmail(email)) {
            return RequestParamInfo.PARAM_EMAIL_STYLE_NO_MEET_NORM;
        }

        return null;
    }

    /**
     * 检测 token
     * @param token
     * @return String
     */
    public static String token(String token){
        if (StringUtils.isEmpty(token)) {
            return RequestParamInfo.PARAM_TOKEN_NO_NULL;
        }

        return null;
    }

    /**
     * 检测 username 和 password
     * @param username
     * @param password
     * @return
     */
    public static String checkUsernamePassword(String username, String password){
        String errorInfo = username(username);
        if (errorInfo != null) {
            return errorInfo;
        }
        errorInfo = password(password);
        if (errorInfo != null) {
            return errorInfo;
        }
        return null;
    }


    /**
     * 检测 username，password，email
     * @param username
     * @param password
     * @param email
     * @return
     */
    public static String checkUsernamePasswordEmail(String username, String password, String email){
        String errorInfo = username(username);
        if (errorInfo != null) {
            return errorInfo;
        }
        errorInfo = password(password);
        if (errorInfo != null) {
            return errorInfo;
        }
        errorInfo = password(email);
        if (errorInfo != null) {
            return errorInfo;
        }

        return null;
    }
}
