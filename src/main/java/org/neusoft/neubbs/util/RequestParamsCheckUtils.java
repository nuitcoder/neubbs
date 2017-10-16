package org.neusoft.neubbs.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Request 请求参数检查 工具类
 *      使用到其余工具类
 *          1. StringUitls.java
 *          2. PatternUtils.java
 */
public class RequestParamsCheckUtils {

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "email";
    private final static String TOKEN = "token";

    private final static String PARAM_USERNAME_NO_NULL = "参数 username，不能为空;";//空判断
    private final static String PARAM_PASSWORD_NO_NULL = " 参数 password，不能为空;";
    private final static String PARAM_EMAIL_NO_NULL = " 参数 email，不能为空;";
    private final static String PARAM_TOKEN_NO_NULL = " 参数 token，不能为空;";

    private final static String PARAM_USERNAME_LENGTH_NO_MATCH_SCOPE = "参数 username ，长度不符合范围（ 3 <= length <= 15）";   //用户名判断
    private final static String PARAM_USERNAME_STYLE_NO_MEET_NORM = "参数 username ，格式不符合规范（A-Z a-z 0-9）";

    private final static String PARAM_PASSWORD_LENGTH_NO_MATCH_SCOPE = "参数 password，长度不符合范围（6 <= length <= 16）";//密码判断

    private final static String PARAM_EMAIL_STYLE_NO_MEET_NORM = "参数 email，邮箱格式不符合规范，请重新输入！";//邮箱判断


    private Map<String, String> requestParamsMap; // 参数键值对
    private StringBuffer errorInfo; //储存错误信息

    /**
     * 检查用户名（通过检查返回null，未通过返回错误信息）
     * @param username
     * @return String
     */
    public static String checkUsername(String username){
        //非空检查
        if(StringUtils.isEmpty(username)){
            return PARAM_USERNAME_NO_NULL;
        }

        //长度检查
        if(!StringUtils.isScope(username, 3, 15)){
            return PARAM_USERNAME_LENGTH_NO_MATCH_SCOPE;
        }

        //正则检查
        if(!PatternUtils.matchUsername(username)){
            return PARAM_USERNAME_STYLE_NO_MEET_NORM;
        }

        return null;
    }

    /**
     * 检查密码
     * @param password
     * @return String
     */
    public static String checkPassword(String password){
        if (StringUtils.isEmpty(password)) {
            return PARAM_PASSWORD_NO_NULL;
        }

        if(!StringUtils.isScope(password, 6, 16)){
            return PARAM_PASSWORD_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 检查邮箱
     * @param email
     * @return String
     */
    public static String checkEmail(String email){
        if(StringUtils.isEmpty(email)){
            return PARAM_EMAIL_NO_NULL;
        }

        if (!PatternUtils.matchEmail(email)) {
            return PARAM_EMAIL_STYLE_NO_MEET_NORM;
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
            return PARAM_TOKEN_NO_NULL;
        }

        return null;
    }

    /**
     * 参数集合非空检测 ，链式调用
     *
     *    String errorInfo = RequestParamsCheckUtils
     *                            .putParamKeys(new String[]{"username", "password", "email"})
     *                            .putParamValues(new String[]{"","",""})
     *                            .checkNull();
     */
    /*** start ***/
    /**
     *  存放参数 key（获取数组参数 key ，规定检查类型，例如：username，password，email）
     * @param paramKeys
     * @return RequestParamsCheckUtils
     */
    public static RequestParamsCheckUtils putParamKeys(String [] paramKeys){
        RequestParamsCheckUtils rpcu = new RequestParamsCheckUtils();
        rpcu.requestParamsMap = new LinkedHashMap<String, String>();

        for(int i = 0 , len = paramKeys.length; i < len; i++){
            rpcu.requestParamsMap.put(paramKeys[i], null);
        }

        return rpcu;
    }

    /**
     * 存放参数 value （获取数组参数 value，将其存入指定 key，例如：map("username", "suvan")）
     * @param paramValues
     * @return RequestParamsCheckUtils
     */
    public RequestParamsCheckUtils putParamValues(String [] paramValues){
        int pointer = 0;
        for(String key: requestParamsMap.keySet()){
           requestParamsMap.put(key, paramValues[pointer++]);
        }

        return this;
    }

    /**
     * 检测参数集合内所有参数的合法性（返回相应错误信息）
     * @return check
     */
    public String checkParamsNorm(){
        StringBuffer errorInfo = new StringBuffer();//储存错误信息

        //非空检测
        String key = null;  //储存参数名
        String value = null; //储存参数值
        for(Map.Entry<String, String> param : requestParamsMap.entrySet()){
            key = param.getKey();
            value = param.getValue();

            if (key.equals(USERNAME)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(PARAM_USERNAME_NO_NULL);
                }
            } else if (key.equals(PASSWORD)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(PARAM_PASSWORD_NO_NULL);
                }
            } else if (key.equals(EMAIL)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(PARAM_EMAIL_NO_NULL);
                }
            }
        }

        if (errorInfo.length() > 0) { //有错误信息直接输出
            return errorInfo.toString();
        }

        //参数合法性检测（长度，正则格式）
        String usernameNormErrorInfo = null;
        String passwordNormErrorInfo = null;
        String emailNormErrorInfo = null;
        key = null;
        value = null;
        for (Map.Entry<String, String> param: requestParamsMap.entrySet()) {
            key = param.getKey();
            value = param.getValue();

            if (key.equals(USERNAME)) {
                usernameNormErrorInfo = checkUsernameNorm(value);
                if (usernameNormErrorInfo != null) {
                    return usernameNormErrorInfo;
                }
            } else if (key.equals(PASSWORD)) {
                passwordNormErrorInfo = checkPasswordNorm(value);
                if (passwordNormErrorInfo != null) {
                    return passwordNormErrorInfo;
                }
            } else if (key.equals(EMAIL)) {
                emailNormErrorInfo = checkEmailNorm(value);
                if (emailNormErrorInfo != null) {
                    return emailNormErrorInfo;
                }
            }
        }

        return null;
    }
    /**
     * 【私有】检测用户名规范（用于链式调用）
     * @param username
     * @return String
     */
    private String checkUsernameNorm(String username){
        if(!StringUtils.isScope(username, 3, 15)){
            return PARAM_USERNAME_LENGTH_NO_MATCH_SCOPE;
        }
        if(!PatternUtils.matchUsername(username)){
            return PARAM_USERNAME_STYLE_NO_MEET_NORM;
        }

        return null;
    }
    /**
     * 【私有】检测密码规范（用于链式调用）
     * @param password
     * @return String
     */
    private String checkPasswordNorm(String password){
        if(!StringUtils.isScope(password, 6, 16)){
            return PARAM_PASSWORD_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }
    /**
     * 【私有】检测邮箱规范（用于链式调用）
     * @param email
     * @return
     */
    private String checkEmailNorm(String email){
        if (!PatternUtils.matchEmail(email)) {
            return PARAM_EMAIL_STYLE_NO_MEET_NORM;
        }

        return null;
    }
    /*** end ***/
}
