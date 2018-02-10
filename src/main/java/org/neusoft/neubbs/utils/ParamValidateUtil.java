package org.neusoft.neubbs.utils;


import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.exception.ParamsErrorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 参数验证工具类
 *      - 检查器
 *
 *  @author Suvan
 */
public final class ParamValidateUtil {

    private ParamValidateUtil() { }

    /*
     * ***********************************************
     * 静态类变量
     * ***********************************************
     */

    /**
     * 请求参数检查长度限制（MIN - MAX）
     *      - 用户名
     *      - 用户密码
     *      - 用户性别
     *      - 用户生日
     *      - 用户所在地
     *      - 用户描述
     *      - 验证码
     *      - ID 参数
     *      - 数字参数
     *      - 话题标题
     *      - 话题分类昵称
     *      - 话题内容
     *      - 回复内容
     *
     *      - 日志打印 MAX 范围（当长度超过此值，后续内容自动缩略符 ......）
     */
     private static final int USERNAME_MIN = 1;
     private static final int USERNAME_MAX = 15;
     private static final int PASSWORD_MIN = 6;
     private static final int PASSWORD_MAX = 16;
     private static final int SEX_MIN = 1;
     private static final int SEX_MAX = 1;
     private static final int BIRTHDAY_MIN = 0;
     private static final int BIRTHDAY_MAX = 20;
     private static final int POSITION_MIN = 0;
     private static final int POSITION_MAX = 235;
     private static final int DESCRIPTION_MIN = 0;
     private static final int DESCRIPTION_MAX = 255;
     private static final int CAPTCHA_MIN = 5;
     private static final int CAPTCHA_MAX = 5;
     private static final int ID_MIN = 1;
     private static final int ID_MAX = 10;
     private static final int NUMBER_MIN = 1;
     private static final int NUMBER_MAX = 10;
     private static final int TOPIC_TITLE_MIN = 1;
     private static final int TOPIC_TITLE_MAX = 55;
     private static final int TOPIC_CATEGORY_NICK_MIN = 1;
     private static final int TOPIC_CATEGORY_NICK_MAX = 20;
     private static final int TOPIC_CONTENT_MIN = 1;
     private static final int TOPIC_CONTENT_MAX = 100000;
     private static final int REPLY_CONTENT_MIN = 1;
     private static final int REPLY_CONTENT_MAX = 150;

     private static final int LOG_LENGTH_MAX = 5;

     /**
      * 存储参数类型检查规则
      *     - 函数通过 static{ } 静态块注册
      *     - 仅作用一次，伴随类加载时加载
      *     - 无需考虑线程安全
      */
     private static Set<String> allowEmptyTypeSet = new HashSet<>();
     private static Map<String, Scope> typeScopeMap = new HashMap<>();
     private static Map<String, Pattern> typePatternMap = new HashMap<>();

    /**
     * Log4j 日志
     */
    private static Logger log = Logger.getRootLogger();


    /*
     * ***********************************************
     * 静态代码块
     *      - 参数空检查
     *          - （参数类型）
     *      - 参数范围检查
     *          - （参数类型，范围类（min，max））
     *      - 参数正则检查
     *          - （参数类型，正则类（'PattenUtil.java' 的函数名，报的日志信息））
     *
     * 【注意】
     *      1. 参数类型，即是 constant\api\ParamConst.java 中的声明类型
     *      2. 当需要添加新的规则，直接在静态代码块中，往相应的集合类型(存储参数类型检查规则)直接添加规则
     *
     * ***********************************************
     */

    static {
        allowEmptyTypeSet.add(ParamConst.BIRTHDAY);
        allowEmptyTypeSet.add(ParamConst.POSITION);
        allowEmptyTypeSet.add(ParamConst.DESCRIPTION);

        typeScopeMap.put(ParamConst.USERNAME, new Scope(USERNAME_MIN, USERNAME_MAX));
        typeScopeMap.put(ParamConst.PASSWORD, new Scope(PASSWORD_MIN, PASSWORD_MAX));
        typeScopeMap.put(ParamConst.SEX, new Scope(SEX_MIN, SEX_MAX));
        typeScopeMap.put(ParamConst.BIRTHDAY, new Scope(BIRTHDAY_MIN, BIRTHDAY_MAX));
        typeScopeMap.put(ParamConst.POSITION, new Scope(POSITION_MIN, POSITION_MAX));
        typeScopeMap.put(ParamConst.DESCRIPTION, new Scope(DESCRIPTION_MIN, DESCRIPTION_MAX));
        typeScopeMap.put(ParamConst.CAPTCHA, new Scope(CAPTCHA_MIN, CAPTCHA_MAX));
        typeScopeMap.put(ParamConst.ID, new Scope(ID_MIN, ID_MAX));
        typeScopeMap.put(ParamConst.NUMBER, new Scope(NUMBER_MIN, NUMBER_MAX));
        typeScopeMap.put(ParamConst.TOPIC_TITLE, new Scope(TOPIC_TITLE_MIN, TOPIC_TITLE_MAX));
        typeScopeMap.put(ParamConst.TOPIC_CATEGORY_NICK, new Scope(TOPIC_CATEGORY_NICK_MIN, TOPIC_CATEGORY_NICK_MAX));
        typeScopeMap.put(ParamConst.TOPIC_CONTENT, new Scope(TOPIC_CONTENT_MIN, TOPIC_CONTENT_MAX));
        typeScopeMap.put(ParamConst.REPLY_CONTENT, new Scope(REPLY_CONTENT_MIN, REPLY_CONTENT_MAX));

        typePatternMap.put(ParamConst.ID, new Pattern("isPureNumber", " （类型）参数不符合规范，必须为纯数字（0 ~ 9）！"));
        typePatternMap.put(ParamConst.NUMBER, new Pattern("isPureNumber", " （类型）参数不符合规范，必须为纯数字(0 ~ 9)！"));
        typePatternMap.put(ParamConst.TOPIC_CATEGORY_NICK,
                new Pattern("isPureEnglish", " （参数）话题分类昵称不符合规范，必须纯英文（a-zA-Z）"));
        typePatternMap.put(ParamConst.USERNAME, new Pattern("matchUsername", "（类型）参数不符合规范（A-Z a-z 0-9）"));
        typePatternMap.put(ParamConst.EMAIL, new Pattern("matchEmail", " （类型）参数不符合规范（xxx@xx.xxx）"));
        typePatternMap.put(ParamConst.AVATOR,
                new Pattern("matchUserAvatarType", " （类型）用户头像文件类型不符合规范（content-type/subtype）"));
    }


    /*
     * ***********************************************
     *  私有静态内部类
     * ***********************************************
     */

    /**
     * 范围类
     */
    private static class Scope {
        int min;
        int max;

        Scope(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * 正则类
     *  - methodName 对应 'PatternUntil.java' 中函数
     *  - logMessage 若不满足匹配规则，则打印日志信息
     */
     private static class Pattern {
        String methodName;
        String logMessage;

        Pattern(String methodName, String logMessage) {
            this.methodName = methodName;
            this.logMessage = logMessage;
        }
    }


     /*
     * ***********************************************
     * public method
     * ***********************************************
     */

    /**
     * 检查器
     *      - 唯一提供外部调用的公共函数
     *          - 若类型不在 allowEmptyTypeSet 之中，则执行空检查（默认不进行空检查）
     *          - 范围检查
     *          - 正则检查
     *
     * @param type 参数类型
     * @param param 参数值
     */
    public static void check(String type, String param) {
        if (!allowEmptyTypeSet.contains(type)) {
            checkNull(type, param);
        }

        checkScope(type, param);
        checkPattern(type, param);
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 空检查
     *
     * @param type 参数类型
     * @param param 参数值
     */
    private static void checkNull(String type, String param) {
        if (StringUtil.isEmpty(param) | SetConst.VALUE_NULL.equals(param)) {
            log.warn(param + " （" + type + " 类型）参数不能为空；");
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR);
        }
    }

    /**
     * 范围检查
     *
     * @param type 参数类型
     * @param param 参数值
     */
    private static void checkScope(String type, String param) {
        Scope scope = typeScopeMap.get(type);
        if (scope == null) {
            return;
        }

        int min = scope.min;
        int max = scope.max;
        if (!StringUtil.isScope(param, min, max)) {
            if (param.length() >= LOG_LENGTH_MAX) {
                param = param.substring(0, LOG_LENGTH_MAX) + "......";
            }

            log.warn(param + " （ " + type + " 类型）长度不符合范围（" + min + " <= length <= " + max + "）");
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR);
        }
    }

    /**
     * 正则检查
     *      - 反射加载 PatternUtil.java 类实例
     *      - 反射执行类中方法（Class<?>-通配泛型，可代表任何类型，Class<T>在实例化的时候，T要替换成具体类）
     *      - method.invoke(null, param) 静态方法不需要借助实例运行，所以为 null
     *
     * @param type 参数类型
     * @param param 参数值
     */
    private static void checkPattern(String type, String param) {
        Pattern pattern = typePatternMap.get(type);
        if (pattern == null) {
            return;
        }

        try {
            Class<?> clazz = Class.forName("org.neusoft.neubbs.utils.PatternUtil");
            Method method = clazz.getDeclaredMethod(pattern.methodName, String.class);

            if (!((boolean) method.invoke(null, param))) {
                log.warn(param + " （ " + type + pattern.logMessage);
                throw new ParamsErrorException(ApiMessage.PARAM_ERROR);
            }
        } catch (NoSuchMethodException | ClassNotFoundException
                | IllegalAccessException |  InvocationTargetException e) {
            log.warn("反射加载 PatternUtils.java 失败，异常出现！");
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR);
        }
    }
}
