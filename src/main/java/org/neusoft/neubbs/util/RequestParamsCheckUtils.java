package org.neusoft.neubbs.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Request 请求参数检查 工具类
 *      使用到其余工具类
 *          1. StringUitls.log
 *          2. PatternUtils.log
 *
 *  @author Suvan
 */
public class RequestParamsCheckUtils {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";
    private static final String CPATCHA = "cpatcha";
    private static final String ID = "id";
    private static final String USERID = "userid";
    private static final String TOPICID = "topicid";
    private static final String CATEGORY = "category";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";

    private static final Integer USERNAME_LENGTH_MIN = 3;
    private static final Integer USERNAME_LENGTH_MAX = 15;
    private static final Integer PASSWORD_LENGTH_MIN = 6;
    private static final Integer PASSWORD_LENGTH_MAX = 16;
    private static final Integer CAPTCHA_LENGTH = 5;
    private static final Integer ONE_LENGTH = 1;
    private static final Integer ID_LENGTH_MAX = 11;
    private static final Integer CATEGORY_LENGTH_MAX = 20;
    private static final Integer TITLE_LENGTH_MAX = 50;
    private static final Integer CONTENT_LENGTH_MAX = 10000;

    /**
     * 错误提示信息
     */
    private static final String WARN_USERNAME_NO_NULL = " 参数 username，不能为空;";
    private static final String WARN_PASSWORD_NO_NULL = " 参数 password，不能为空;";
    private static final String WARN_EMAIL_NO_NULL = " 参数 email，不能为空;";
    private static final String WARN_TOKEN_NO_NULL = " 参数 token，不能为空;";
    private static final String WARN_CAPTCHA_NO_NULL = " 参数 captcha，不能为空；";
    private static final String WARN_ID_NO_NULL = "参数 id，不能为空;";
    private static final String WARN_CATEGORY_NO_NULL = "参数 category，不能为空；";
    private static final String WARN_TITLE_NO_NULL = "参数 title，不能为空；";
    private static final String WARN_CONTENT_NO_NULL = "参数 content，不能为空；";

    private static final String WARN_USERNAME_LENGTH_NO_MATCH_SCOPE = "参数 username ，长度不符合范围（ 3 <= length <= 15）";
    private static final String WARN_PASSWORD_LENGTH_NO_MATCH_SCOPE = "参数 password，长度不符合范围（6 <= length <= 16）";
    private static final String WARN_CAPTCHA_LENGTH_NO_MATCH_SCOPE = " 参数 captcha，长度不符合匹配(length == 5)";
    private static final String WARN_ID_LENGTH_NO_MATCH_SCOPE = "参数 id，长度不匹配指定范围（1 <= length <= 11）";
    private static final String WARN_CATEGORY_LENGTH_NO_MATCH_SCOPE = "参数 category，长度不符合范围（1 <= length <= 20）";
    private static final String WARN_TITLE_LENGTH_NO_MATCH_SCOPE = "参数 title，长度不符合规范（1 <= length <= 50）";
    private static final String WARN_CONTENT_LENGTH_NO_MATCH_SCOPE = "参数 content，长度不符合规范（1 <= length < 10000）";

    private static final String WARN_USERNAME_STYLE_NO_MEET_NORM = "参数 username ，格式不符合规范（A-Z a-z 0-9），请重新输入！";
    private static final String WARN_EMAIL_STYLE_NO_MEET_NORM = "参数 email，邮箱格式不符合规范（xxx@xx.xxx），请重新输入！";
    private static final String WARN_CATEGORY_STYLE_NO_MEET_NORM = "参数 category，格式不规范（仅包含中英文，不能有数字 or 特殊字符），请重新输入！";

    private Map<String, String> requestParamsMap;

    /**
     * 检查用户名
     *
     * @param username 用户名
     * @return String 错误信息
     */
    public static String checkUsername(String username){
        //非空检查
        if(StringUtils.isEmpty(username)){
            return WARN_USERNAME_NO_NULL;
        }

        //长度检查
        if(!StringUtils.isScope(username, USERNAME_LENGTH_MIN, USERNAME_LENGTH_MAX)){
            return WARN_USERNAME_LENGTH_NO_MATCH_SCOPE;
        }

        //正则检查
        if(!PatternUtils.matchUsername(username)){
            return WARN_USERNAME_STYLE_NO_MEET_NORM;
        }

        return null;
    }

    /**
     * 检查密码
     *
     * @param password 用户密码
     * @return String 错误信息
     */
    public static String checkPassword(String password){
        if (StringUtils.isEmpty(password)) {
            return WARN_PASSWORD_NO_NULL;
        }

        if(!StringUtils.isScope(password, PASSWORD_LENGTH_MIN, PASSWORD_LENGTH_MAX)){
            return WARN_PASSWORD_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 检查邮箱
     *
     * @param email 用户邮箱
     * @return String 错误信息
     */
    public static String checkEmail(String email){
        if(StringUtils.isEmpty(email)){
            return WARN_EMAIL_NO_NULL;
        }

        if (!PatternUtils.matchEmail(email)) {
            return WARN_EMAIL_STYLE_NO_MEET_NORM;
        }

        return null;
    }

    /**
     * 检测 token
     *
     * @param token 密文
     * @return String 错误信息
     */
    public static String checkToken(String token){
        if (StringUtils.isEmpty(token)) {
            return WARN_TOKEN_NO_NULL;
        }

        return null;
    }

    /**
     * 检测 captcha
     * @param captcha 验证码
     * @return String 错误信息
     */
    public static String checkCaptcha(String captcha){
        if (StringUtils.isEmpty(captcha)) {
            return WARN_CAPTCHA_NO_NULL;
        }

        if (!StringUtils.isScope(captcha, CAPTCHA_LENGTH, CAPTCHA_LENGTH)) {
            return WARN_CAPTCHA_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 检查 id
     *
     * @param id id类型参数
     * @return String 错误信息
     */
    public static String checkId(String id){
        if (StringUtils.isEmpty(id) || "null".equals(id)) {
            return WARN_ID_NO_NULL;
        }

        if (!StringUtils.isScope(id, ONE_LENGTH, ID_LENGTH_MAX)) {
            return WARN_ID_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 检查话题类别
     *
     * @param category 话题分类
     * @return String 错误信息
     */
    public static String checkCategory(String category){
        if (StringUtils.isEmpty(category)) {
            return WARN_CAPTCHA_NO_NULL;
        }

        if (!StringUtils.isScope(category, ONE_LENGTH, CATEGORY_LENGTH_MAX)) {
            return WARN_CATEGORY_LENGTH_NO_MATCH_SCOPE;
        }

        if (!PatternUtils.matchTopicCategory(category)) {
            return WARN_CATEGORY_STYLE_NO_MEET_NORM;
        }

        return null;
    }

    /**
     * 检查话题标题
     *
     * @param title 话题标题
     * @return String 错误信息
     */
    public static String checkTitle(String title){
        if (StringUtils.isEmpty(title)) {
            return WARN_TITLE_NO_NULL;
        }

        if (StringUtils.isScope(title, ONE_LENGTH, TITLE_LENGTH_MAX)) {
            return WARN_TITLE_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 检查话题内容
     *
     * @param content 话题内容
     * @return String 错误信息
     */
    public static String checkContent(String content){
        if (StringUtils.isEmpty(content)) {
            return WARN_CONTENT_NO_NULL;
        }

        if (StringUtils.isScope(content, ONE_LENGTH, CONTENT_LENGTH_MAX)) {
            return WARN_CAPTCHA_LENGTH_NO_MATCH_SCOPE;
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
     *
     * @param paramKeys 需要检测的类型参数数组
     * @return RequestParamsCheckUtils 工具类自调用
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
     *
     * @param paramValues 参数 Value 数组
     * @return RequestParamsCheckUtils 工具类自调用
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
     *
     * @return String 错误信息
     */
    public String checkParamsNorm(){
        //储存错误信息
        StringBuilder errorInfo = new StringBuilder();

        //非空检测
        String key = null;
        String value = null;
        for(Map.Entry<String, String> param : requestParamsMap.entrySet()){
            key = param.getKey();
            value = param.getValue();

            if (key.equals(USERNAME)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(WARN_USERNAME_NO_NULL);
                }
            } else if (key.equals(PASSWORD)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(WARN_PASSWORD_NO_NULL);
                }
            } else if (key.equals(EMAIL)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(WARN_EMAIL_NO_NULL);
                }
            } else if (key.equals(ID) || key.equals(USERID) || key.equals(TOPICID)) {
                if (StringUtils.isEmpty(value) || "null".equals(value)) {
                    errorInfo.append(WARN_ID_NO_NULL);
                }
            } else if (key.equals(CATEGORY)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(WARN_CATEGORY_NO_NULL);
                }
            } else if (key.equals(TITLE)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(WARN_TITLE_NO_NULL);
                }
            } else if (key.equals(CONTENT)) {
                if (StringUtils.isEmpty(value)) {
                    errorInfo.append(WARN_CONTENT_NO_NULL);
                }
            }
        }

        //有错误信息直接输出
        if (errorInfo.length() > 0) {
            return errorInfo.toString();
        }

        //参数合法性检测（长度，正则格式）
        String usernameNormErrorInfo = null;
        String passwordNormErrorInfo = null;
        String emailNormErrorInfo = null;
        String idNormErrofInfo = null;
        String categoryNormErrofrInfo = null;
        String titleNormErrorInfo = null;
        String contentNormErrorInfo = null;
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
            } else if (key.equals(ID) || key.equals(USERID) || key.equals(TOPICID)) {
                idNormErrofInfo = checkIdNorm(value);
                if (idNormErrofInfo != null) {
                    return idNormErrofInfo;
                }
            } else if (key.equals(CATEGORY)) {
                categoryNormErrofrInfo = checkCategoryNorm(value);
                if (categoryNormErrofrInfo != null) {
                    return categoryNormErrofrInfo;
                }
            } else if (key.equals(TITLE)) {
                titleNormErrorInfo = checkTitleNorm(value);
                if (titleNormErrorInfo != null) {
                    return titleNormErrorInfo;
                }
            } else if (key.equals(CONTENT)) {
                contentNormErrorInfo = checkContentNorm(value);
                if (categoryNormErrofrInfo != null) {
                    return contentNormErrorInfo;
                }
            }
        }

        return null;
    }
    /**
     * 【私有】检测用户名规范（用于链式调用）
     *
     * @param username 用户名
     * @return String 错误信息
     */
    private String checkUsernameNorm(String username){
        if(!StringUtils.isScope(username, USERNAME_LENGTH_MIN, USERNAME_LENGTH_MAX)){
            return WARN_USERNAME_LENGTH_NO_MATCH_SCOPE;
        }
        if(!PatternUtils.matchUsername(username)){
            return WARN_USERNAME_STYLE_NO_MEET_NORM;
        }

        return null;
    }
    /**
     * 【私有】检测密码规范（用于链式调用）
     *
     * @param password 用户密码
     * @return String 错误信息
     */
    private String checkPasswordNorm(String password){
        if(!StringUtils.isScope(password, PASSWORD_LENGTH_MIN, PASSWORD_LENGTH_MAX)){
            return WARN_PASSWORD_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }
    /**
     * 【私有】检测邮箱规范（用于链式调用）
     *
     * @param email 用户邮箱
     * @return 错误信息
     */
    private String checkEmailNorm(String email){
        if (!PatternUtils.matchEmail(email)) {
            return WARN_EMAIL_STYLE_NO_MEET_NORM;
        }

        return null;
    }

    /**
     * 【私有】检查 id 规范（用于链式调用）
     *
     * @param id id类型参数
     * @return String 错误信息
     */
    private static String checkIdNorm(String id){
        if (!StringUtils.isScope(id, ONE_LENGTH, ID_LENGTH_MAX)) {
            return WARN_ID_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 【私有】检查话题类别（用于链式调用）
     *
     * @param category 话题分类
     * @return String 错误信息
     */
    private static String checkCategoryNorm(String category){
        if (!StringUtils.isScope(category, ONE_LENGTH, CATEGORY_LENGTH_MAX)) {
            return WARN_CATEGORY_LENGTH_NO_MATCH_SCOPE;
        }

        if (!PatternUtils.matchTopicCategory(category)) {
            return WARN_CATEGORY_STYLE_NO_MEET_NORM;
        }

        return null;
    }

    /**
     * 【私有】检查话题标题（用于链式调用）
     *
     * @param title 话题标题
     * @return String 错误信息
     */
    private static String checkTitleNorm(String title){
        if (!StringUtils.isScope(title, ONE_LENGTH, TITLE_LENGTH_MAX)) {
            return WARN_TITLE_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 【私有】检查话题内容（用于链式调用）
     *
     * @param content 话题内容
     * @return String 错误信息
     */
    private static String checkContentNorm(String content){
        if (!StringUtils.isScope(content, ONE_LENGTH, CONTENT_LENGTH_MAX)) {
            return WARN_CAPTCHA_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }
    /*** end ***/
}
