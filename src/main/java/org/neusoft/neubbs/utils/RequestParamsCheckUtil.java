package org.neusoft.neubbs.utils;

import com.sun.org.apache.regexp.internal.RE;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Request 请求参数检查 工具类
 *      使用到其余工具类
 *          1. StringUitls.log
 *          2. PatternUtil.log
 *
 *  @author Suvan
 */
public final class RequestParamsCheckUtil {
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
    private static final String REPLY = "reply";

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
    private static final Integer REPLY_LENGTH_MAX = 150;

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
    private static final String WARN_REPLY_CONTENT_NO_NULL = "参数 reply content 不能为空";

    private static final String WARN_USERNAME_LENGTH_NO_MATCH_SCOPE = "参数 username ，长度不符合范围（ 3 <= length <= 15）";
    private static final String WARN_PASSWORD_LENGTH_NO_MATCH_SCOPE = "参数 password，长度不符合范围（6 <= length <= 16）";
    private static final String WARN_CAPTCHA_LENGTH_NO_MATCH_SCOPE = " 参数 captcha，长度不符合匹配(length == 5)";
    private static final String WARN_ID_LENGTH_NO_MATCH_SCOPE = "参数 id，长度不匹配指定范围（1 <= length <= 11）";
    private static final String WARN_CATEGORY_LENGTH_NO_MATCH_SCOPE = "参数 category，长度不符合范围（1 <= length <= 20）";
    private static final String WARN_TITLE_LENGTH_NO_MATCH_SCOPE = "参数 title，长度不符合规范（1 <= length <= 50）";
    private static final String WARN_CONTENT_LENGTH_NO_MATCH_SCOPE = "参数 content，长度不符合规范（1 <= length < 10000）";
    private static final String WARN_REPLY_CONTENT_LENGTH_NO_MATCH_SCOPE = "参数 reply content 长度不符合规范（1 <= length < 150）";

    private static final String WARN_USERNAME_STYLE_NO_MEET_NORM = "参数 username ，格式不符合规范（A-Z a-z 0-9），请重新输入！";
    private static final String WARN_EMAIL_STYLE_NO_MEET_NORM = "参数 email，邮箱格式不符合规范（xxx@xx.xxx），请重新输入！";
    private static final String WARN_CATEGORY_STYLE_NO_MEET_NORM = "参数 category，格式不规范"
                                                                + "（仅包含中英文，不能有数字 or 特殊字符），请重新输入！";

    private Map<String, String> requestParamsMap;

    private RequestParamsCheckUtil() {

    }

    /**
     * 检查用户名
     *      1.非空检查
     *      2.长度检查
     *      3.正则检查
     *
     * @param username 用户名
     * @return String 错误信息
     */
    public static String checkUsername(String username) {
        String errorInfo  = null;

        if (StringUtil.isEmpty(username)) {
            errorInfo = WARN_USERNAME_NO_NULL;
        } else if (!StringUtil.isScope(username, USERNAME_LENGTH_MIN, USERNAME_LENGTH_MAX)) {
            errorInfo = WARN_USERNAME_LENGTH_NO_MATCH_SCOPE;
        } else if (!PatternUtil.matchUsername(username)) {
            errorInfo = WARN_USERNAME_STYLE_NO_MEET_NORM;
        }

        return errorInfo;
    }

    /**
     * 检查密码
     *      1.非空
     *      2.长度
     *
     * @param password 用户密码
     * @return String 错误信息
     */
    public static String checkPassword(String password) {
        String errorInfo = null;

        if (StringUtil.isEmpty(password)) {
            errorInfo = WARN_PASSWORD_NO_NULL;
        } else if (!StringUtil.isScope(password, PASSWORD_LENGTH_MIN, PASSWORD_LENGTH_MAX)) {
            errorInfo =  WARN_PASSWORD_LENGTH_NO_MATCH_SCOPE;
        }

        return errorInfo;
    }

    /**
     * 检查邮箱
     *      1.非空
     *      2.长度
     *
     * @param email 用户邮箱
     * @return String 错误信息
     */
    public static String checkEmail(String email) {
        String errorInfo = null;

        if (StringUtil.isEmpty(email)) {
            errorInfo = WARN_EMAIL_NO_NULL;
        } else if (!PatternUtil.matchEmail(email)) {
            errorInfo = WARN_EMAIL_STYLE_NO_MEET_NORM;
        }

        return errorInfo;
    }

    /**
     * 检测 token
     *      1.非空
     *
     * @param token 密文
     * @return String 错误信息
     */
    public static String checkToken(String token) {
        if (StringUtil.isEmpty(token)) {
            return WARN_TOKEN_NO_NULL;
        }

        return null;
    }

    /**
     * 检测 captcha
     *      1.非空
     *      2.长度
     *
     * @param captcha 验证码
     * @return String 错误信息
     */
    public static String checkCaptcha(String captcha) {
        String errorInfo = null;

        if (StringUtil.isEmpty(captcha)) {
            errorInfo = WARN_CAPTCHA_NO_NULL;
        } else if (!StringUtil.isScope(captcha, CAPTCHA_LENGTH, CAPTCHA_LENGTH)) {
            errorInfo = WARN_CAPTCHA_LENGTH_NO_MATCH_SCOPE;
        }

        return errorInfo;
    }

    /**
     * 检查 id
     *      1.非空
     *      2.长度
     *
     * @param id id类型参数
     * @return String 错误信息
     */
    public static String checkId(String id) {
        String errorInfo = null;

        if (StringUtil.isEmpty(id) || "null".equals(id)) {
            errorInfo = WARN_ID_NO_NULL;
        } else if (!StringUtil.isScope(id, ONE_LENGTH, ID_LENGTH_MAX)) {
            errorInfo = WARN_ID_LENGTH_NO_MATCH_SCOPE;
        }

        return errorInfo;
    }

    /**
     * 检查话题类别
     *      1.长度
     *      2.非空
     *      3.正则
     *
     * @param category 话题分类
     * @return String 错误信息
     */
    public static String checkCategory(String category) {
        String errorInfo = null;

        if (StringUtil.isEmpty(category)) {
            errorInfo = WARN_CAPTCHA_NO_NULL;
        } else if (!StringUtil.isScope(category, ONE_LENGTH, CATEGORY_LENGTH_MAX)) {
            errorInfo = WARN_CATEGORY_LENGTH_NO_MATCH_SCOPE;
        } else if (!PatternUtil.matchTopicCategory(category)) {
            errorInfo = WARN_CATEGORY_STYLE_NO_MEET_NORM;
        }

        return errorInfo;
    }

    /**
     * 检查话题标题
     *
     * @param title 话题标题
     * @return String 错误信息
     */
    public static String checkTitle(String title) {
        if (StringUtil.isEmpty(title)) {
            return WARN_TITLE_NO_NULL;
        }

        if (StringUtil.isScope(title, ONE_LENGTH, TITLE_LENGTH_MAX)) {
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
    public static String checkContent(String content) {
        if (StringUtil.isEmpty(content)) {
            return WARN_CONTENT_NO_NULL;
        }

        if (StringUtil.isScope(content, ONE_LENGTH, CONTENT_LENGTH_MAX)) {
            return WARN_CAPTCHA_LENGTH_NO_MATCH_SCOPE;
        }

        return null;
    }

    /**
     * 检查回复内容
     *
     * @param content 回复内容
     * @return String 错误信息
     */
    public static String checkReply(String content) {
        if (StringUtil.isEmpty(content)) {
            return WARN_REPLY_CONTENT_NO_NULL;
        }

       if(StringUtil.isScope(content, ONE_LENGTH, CONTENT_LENGTH_MAX)) {
            return WARN_REPLY_CONTENT_LENGTH_NO_MATCH_SCOPE;
       }

        return null;
    }

    /**
     * 参数集合非空检测 ，链式调用
     *
     *    String errorInfo = RequestParamsCheckUtil
     *                            .putParamKeys(new String[]{"username", "password", "email"})
     *                            .putParamValues(new String[]{"","",""})
     *                            .checkNull();
     */
    /*** start ***/
    /**
     *  存放参数 key（获取数组参数 key ，规定检查类型，例如：username，password，email）
     *
     * @param paramKeys 需要检测的类型参数数组
     * @return RequestParamsCheckUtil 工具类自调用
     */
    public static RequestParamsCheckUtil putParamKeys(String[] paramKeys) {
        RequestParamsCheckUtil rpcu = new RequestParamsCheckUtil();
            rpcu.requestParamsMap = new LinkedHashMap<>();

        for (int i = 0, len = paramKeys.length; i < len; i++) {
            rpcu.requestParamsMap.put(paramKeys[i], null);
        }

        return rpcu;
    }

    /**
     * 存放参数 value （获取数组参数 value，将其存入指定 key，例如：map("username", "suvan")）
     *
     * @param paramValues 参数 Value 数组
     * @return RequestParamsCheckUtil 工具类自调用
     */
    public RequestParamsCheckUtil putParamValues(String[] paramValues) {
        int pointer = 0;
        for (String key: requestParamsMap.keySet()) {
           requestParamsMap.put(key, paramValues[pointer++]);
        }

        return this;
    }

    /**
     * 检测参数集合内所有参数的合法性（返回相应错误信息）
     *
     * @return String 错误信息
     */
    public String checkParamsNorm() {
        StringBuilder errorInfo = new StringBuilder();

        //参数空检测
        String nullErrorInfo = paramsNullNorm();
        if (nullErrorInfo != null) {
            return nullErrorInfo;
        }

        //参数合法性检测（长度，正则格式）
        errorInfo.setLength(0);
        String key = null;
        String value = null;
        for (Map.Entry<String, String> param: requestParamsMap.entrySet()) {
            key = param.getKey();
            value = param.getValue();

            if (key.equals(USERNAME)) {
                errorInfo.append(checkUsernameNorm(value));
            } else if (key.equals(PASSWORD)) {
                errorInfo.append(checkPasswordNorm(value));
            } else if (key.equals(EMAIL)) {
                errorInfo.append(checkEmailNorm(value));
            } else if (key.equals(ID) || key.equals(USERID) || key.equals(TOPICID)) {
                errorInfo.append(checkIdNorm(value));
            } else if (key.equals(CATEGORY)) {
                errorInfo.append(checkCategoryNorm(value));
            } else if (key.equals(TITLE)) {
                errorInfo.append(checkTitleNorm(value));
            } else if (key.equals(CONTENT)) {
                errorInfo.append(checkCategoryNorm(value));
            } else if (key.equals(REPLY)) {
                errorInfo.append(checkReplyNorm(value));
            }

            if (errorInfo.length() > 0) {
                return errorInfo.toString();
            }
        }

        return null;
    }

    /**
     * 【私有】参数集合空检测
     *
     * @return String 错误信息
     */
    private String paramsNullNorm() {
        //储存错误信息
        StringBuilder errorInfo = new StringBuilder();

        //非空检测
        String key = null;
        String value = null;
        for (Map.Entry<String, String> param : requestParamsMap.entrySet()) {
            key = param.getKey();
            value = param.getValue();

            if (key.equals(USERNAME)) {
                if (StringUtil.isEmpty(value)) {
                    errorInfo.append(WARN_USERNAME_NO_NULL);
                }
            } else if (key.equals(PASSWORD)) {
                if (StringUtil.isEmpty(value)) {
                    errorInfo.append(WARN_PASSWORD_NO_NULL);
                }
            } else if (key.equals(EMAIL)) {
                if (StringUtil.isEmpty(value)) {
                    errorInfo.append(WARN_EMAIL_NO_NULL);
                }
            } else if (key.equals(ID) || key.equals(USERID) || key.equals(TOPICID)) {
                if (StringUtil.isEmpty(value) || "null".equals(value)) {
                    errorInfo.append(WARN_ID_NO_NULL);
                }
            } else if (key.equals(CATEGORY)) {
                if (StringUtil.isEmpty(value)) {
                    errorInfo.append(WARN_CATEGORY_NO_NULL);
                }
            } else if (key.equals(TITLE)) {
                if (StringUtil.isEmpty(value)) {
                    errorInfo.append(WARN_TITLE_NO_NULL);
                }
            } else if (key.equals(CONTENT)) {
                if (StringUtil.isEmpty(value)) {
                    errorInfo.append(WARN_CONTENT_NO_NULL);
                }
            } else if (key.equals(REPLY)) {
                if (StringUtil.isEmpty(value)) {
                    errorInfo.append(WARN_REPLY_CONTENT_NO_NULL);
                }
            }

            //有错误信息直接输出
            if (errorInfo.length() > 0) {
                return errorInfo.toString();
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
    private String checkUsernameNorm(String username) {
        if (!StringUtil.isScope(username, USERNAME_LENGTH_MIN, USERNAME_LENGTH_MAX)) {
            return WARN_USERNAME_LENGTH_NO_MATCH_SCOPE;
        }
        if (!PatternUtil.matchUsername(username)) {
            return WARN_USERNAME_STYLE_NO_MEET_NORM;
        }

        return "";
    }
    /**
     * 【私有】检测密码规范（用于链式调用）
     *
     * @param password 用户密码
     * @return String 错误信息
     */
    private String checkPasswordNorm(String password) {
        if (!StringUtil.isScope(password, PASSWORD_LENGTH_MIN, PASSWORD_LENGTH_MAX)) {
            return WARN_PASSWORD_LENGTH_NO_MATCH_SCOPE;
        }

        return "";
    }
    /**
     * 【私有】检测邮箱规范（用于链式调用）
     *
     * @param email 用户邮箱
     * @return 错误信息
     */
    private String checkEmailNorm(String email) {
        if (!PatternUtil.matchEmail(email)) {
            return WARN_EMAIL_STYLE_NO_MEET_NORM;
        }

        return "";
    }

    /**
     * 【私有】检查 id 规范（用于链式调用）
     *
     * @param id id类型参数
     * @return String 错误信息
     */
    private static String checkIdNorm(String id) {
        if (!StringUtil.isScope(id, ONE_LENGTH, ID_LENGTH_MAX)) {
            return WARN_ID_LENGTH_NO_MATCH_SCOPE;
        }

        return "";
    }

    /**
     * 【私有】检查话题类别（用于链式调用）
     *
     * @param category 话题分类
     * @return String 错误信息
     */
    private static String checkCategoryNorm(String category) {
        if (!StringUtil.isScope(category, ONE_LENGTH, CATEGORY_LENGTH_MAX)) {
            return WARN_CATEGORY_LENGTH_NO_MATCH_SCOPE;
        }

        if (!PatternUtil.matchTopicCategory(category)) {
            return WARN_CATEGORY_STYLE_NO_MEET_NORM;
        }

        return "";
    }

    /**
     * 【私有】检查话题标题（用于链式调用）
     *
     * @param title 话题标题
     * @return String 错误信息
     */
    private static String checkTitleNorm(String title) {
        if (!StringUtil.isScope(title, ONE_LENGTH, TITLE_LENGTH_MAX)) {
            return WARN_TITLE_LENGTH_NO_MATCH_SCOPE;
        }

        return "";
    }

    /**
     * 【私有】检查话题内容（用于链式调用）
     *
     * @param content 话题内容
     * @return String 错误信息
     */
    private static String checkContentNorm(String content) {
        if (!StringUtil.isScope(content, ONE_LENGTH, CONTENT_LENGTH_MAX)) {
            return WARN_CAPTCHA_LENGTH_NO_MATCH_SCOPE;
        }

        return "";
    }

    /**
     * 【私有】检查回复内容（用于链式调用）
     *
     * @param content 话题内容
     * @return String 错误信息
     */
    private static String checkReplyNorm(String content) {
        if (!StringUtil.isScope(content, ONE_LENGTH, REPLY_LENGTH_MAX)) {
            return WARN_REPLY_CONTENT_LENGTH_NO_MATCH_SCOPE;
        }

        return "";
    }
    /*** end ***/
}
