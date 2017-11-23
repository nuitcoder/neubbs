package org.neusoft.neubbs.utils;


import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Request 请求参数检查 工具类
 *      使用到其余工具类
 *          1. StringUitls.log
 *          2. PatternUtil.log
 *
 *  @author Suvan
 */
public final class RequestParamCheckUtil {

    private RequestParamCheckUtil() { }

    private static final Integer ONE = 1;
    private static final Integer THREE = 3;
    private static final Integer FIVE = 5;
    private static final Integer SIX = 6;
    private static final Integer ELEVEN = 11;
    private static final Integer FIFTEEN = 15;
    private static final Integer SIXTEEN = 16;
    private static final Integer TWENTY = 20;
    private static final Integer FIFTY = 50;
    private static final Integer ONE_HUNDRED_FIFTY = 150;
    private static final Integer TEN_THOUSAND = 10000;

    private static final String NULL = "null";


    private static Map<String, Scope> typeScopeMap = new HashMap<>();
    private static Map<String, Pattern> typePatternMap = new HashMap<>();

    /**
     * 储存范围，最大值与最小值
     */
    static class Scope {
        int min;
        int max;

        Scope(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * 储存正则使用，PatternUtils 工具类中方法名 和 日志信息
     */
    static class Pattern {
        String methodName;
        String logMessage;

        Pattern(String methodName, String logMessage) {
            this.methodName = methodName;
            this.logMessage = logMessage;
        }
    }

    static {
        /*
         * 静态代码块
         *      - 注册需 “范围检查” 的类型
         *      - 注册需 “格式检查” 的类型
         */
        typeScopeMap.put(ParamConst.USERNAME, new Scope(THREE, FIFTEEN));
        typeScopeMap.put(ParamConst.PASSWORD, new Scope(SIX, SIXTEEN));
        typeScopeMap.put(ParamConst.CAPTCHA, new Scope(FIVE, FIVE));
        typeScopeMap.put(ParamConst.ID, new Scope(ONE, ELEVEN));
        typeScopeMap.put(ParamConst.TITLE, new Scope(ONE, FIFTY));
        typeScopeMap.put(ParamConst.CATEGORY, new Scope(ONE, TWENTY));
        typeScopeMap.put(ParamConst.TOPIC_CONTENT, new Scope(ONE, TEN_THOUSAND));
        typeScopeMap.put(ParamConst.REPLY_CONTENT, new Scope(ONE, ONE_HUNDRED_FIFTY));

        typePatternMap.put(ParamConst.ID, new Pattern("isPureNumber", " （类型）参数不符合规范，必须为数字！"));
        typePatternMap.put(ParamConst.NUMBER, new Pattern("isPureNumber", " （类型）参数不符合规范，必须为数字！"));
        typePatternMap.put(ParamConst.USERNAME, new Pattern("matchUsername", "（类型）参数不符合规范（A-Z a-z 0-9）"));
        typePatternMap.put(ParamConst.EMAIL, new Pattern("matchEmail", " （类型）参数不符合规范（xxx@xx.xxx）"));
        typePatternMap.put(ParamConst.CATEGORY,
                new Pattern("matchTopicCategory", " （类型）参数不规范（仅包含中英文，不能有数字 or 特殊字符）"));
    }


    /**
     * 检查器
     *
     * @param type 参数类型
     * @param param 参数值
     * @throws ParamsErrorException 参数错误异常
     */
    public static void check(String type, String param) throws ParamsErrorException {
        //空检查
        checkNull(type, param);

        //范围检查
        checkScope(type, param);

        //格式检查
        checkPattern(type, param);
    }

    /**
     * 检查器
     *
     * @param typeParamMap 类型-参数，键值对
     * @throws ParamsErrorException 参数错误异常
     */
    public static void check(Map<String, String> typeParamMap) throws ParamsErrorException {
        //统一空检查
        for (Map.Entry<String, String> entry : typeParamMap.entrySet()) {
            checkNull(entry.getKey(), entry.getValue());
        }

        //根据不同参数类型，进行相应格式检查
        for (Map.Entry<String, String> entry : typeParamMap.entrySet()) {
            checkScope(entry.getKey(), entry.getValue());
            checkPattern(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 空检查
     *
     * @param type 参数类型
     * @param param 参数值
     * @throws ParamsErrorException 参数错误异常
     */
    private static void checkNull(String type, String param) throws ParamsErrorException {
        if (StringUtil.isEmpty(param) | NULL.equals(param)) {
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(param + " （" + type + " 类型）参数不能为空；");
        }
    }

    /**
     * 范围检查
     *
     * @param type 参数类型
     * @param param 参数值
     * @throws ParamsErrorException 参数错误异常
     */
    private static void checkScope(String type, String param) throws ParamsErrorException {
        Scope scope = typeScopeMap.get(type);
        if (scope == null) {
            return;
        }

        int min = scope.min;
        int max = scope.max;
        if (!StringUtil.isScope(param, min, max)) {
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR)
                    .log(param + " （ " + type + " 类型）长度不符合范围（" + min + " <= length <=" + max + "）");
        }
    }

    /**
     * 格式检查
     *
     * @param type 参数类型
     * @param param 参数值
     * @throws ParamsErrorException 参数错误异常
     */
    private static void checkPattern(String type, String param) throws ParamsErrorException {
        Pattern pattern = typePatternMap.get(type);
        if (pattern == null) {
            return;
        }

        try {
            //反射执行类中方法（Class<?>-通配泛型，可代表任何类型，Class<T>在实例化的时候，T要替换成具体类）
            Class<?> clazz = Class.forName("org.neusoft.neubbs.utils.PatternUtil");

            Method method = clazz.getDeclaredMethod(pattern.methodName, String.class);
            //静态方法不需要借助实例运行，所以为 null
            if (!((boolean) method.invoke(null, param))) {
                throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(param + " （ " + type + pattern.logMessage);
            }
        } catch (NoSuchMethodException | ClassNotFoundException
                | IllegalAccessException |  InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
