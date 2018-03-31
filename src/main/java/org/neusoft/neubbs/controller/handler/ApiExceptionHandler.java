package org.neusoft.neubbs.controller.handler;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;
import org.neusoft.neubbs.exception.IPrintLog;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * api 异常处理器
 *      - 继承 Spring 异常处理解析器，异常抛出时调用，经由此类，统一处理整个项目所抛出的异常,
 *      - 根据相应异常打印 log，若未自定义异常，页面返回 unknown error, 并服务端控制台打印异常
 *
 *     【未完善的点】
 *      1. 如果不是自定以异常，属于未知，那么异常栈 log 追踪 【reason position】:会定位到未知异常的点（可能在类库的某一行）
 *        例如如：(Integer.parseInt("字符串"))，而无法定位到某层（Controller, Service, Util ....），因为只追踪第一层的信息
 *
 *      2. log4j 默认打印日志信息都会定位到 HandlerExceptionResolver 内部（无法定位到指定行）
 *        例如：[WARN]-[2018-03-31 09:42:13]-[http-nio-8080-exec-8 thread]-[ApiExceptionHandler.java (143 row)]
 *
 * @author suvan
 */
public class ApiExceptionHandler implements HandlerExceptionResolver {

    private Logger logger = Logger.getRootLogger();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object o, Exception e) {

        //judge whether is customized exception（exist @ApiException）
        boolean isCustomApiException = e.getClass().getAnnotation(ApiException.class) != null;

        //page response fail message
        if (isCustomApiException) {
            this.outFailJsonMessage(response, e.getMessage());
            this.logCustomizedApiException(e);
        } else if (e instanceof ClassCastException) {
            //request params type convert exception
            this.outFailJsonMessage(response, ApiMessage.PARAM_ERROR);
            this.logException("访问 " + request.getRequestURI() + " 接口，传入参数类型错误！请参考《后端 API 交互协议》文档！", e);

        } else {
            //unknown exception, output unknown error
            this.outFailJsonMessage(response, ApiMessage.UNKNOWN_ERROR);
            this.logException("服务端出现我未知错误, 已邮件通知开发人员开发人员尽快处理！", e);
        }

        return null;
    }


    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 输出页面失败信息
     *
     * @param response http 响应
     * @param failMessage 页面 ”message“ 字段的失败信息
     */
    private void outFailJsonMessage(HttpServletResponse response, String failMessage) {
        //set response headers
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        //build response page output json map
        Map<String, Object> map = new LinkedHashMap<>(SetConst.SIZE_THREE);
            map.put("success", false);
            map.put("message", failMessage);
            map.put("model", new HashMap<>(0));

        //print writer output stream
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(JsonUtil.toJsonString(map));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * 记录自定义 Api 异常信息
     *      - 仅针对当前项目（Neubbs）内，自定义的异常
     *      - 判断是否实现 IPrintLog 接口，若实现，强转执行打印异常
     *
     * @param apiException 自定义的 api 异常对象
     */
    private void logCustomizedApiException(Exception apiException) {
        //print api exception to local log
        if (apiException instanceof IPrintLog) {
            LogWarnEnum logWarnEnum = ((IPrintLog) apiException).getLog();
            String logMessage = logWarnEnum != null ? logWarnEnum.getErrorMessage() : null;

            if (logMessage != null) {
                //get exception stack root element (pass stacktrace)
                this.logExceptionStackMessage(logMessage, this.traceExceptionStackFirstElement(apiException));
            }
        } else {
            //log common exception
            this.logException(apiException.getMessage(), apiException);
        }

    }

    /**
     * 打印异常信息
     *      - 针对非自定义异常，未知异常
     *
     * @param logMessage 日志信息
     * @param e 异常对象
     */
    private void logException(String logMessage, Exception e) {
        this.logExceptionStackMessage(logMessage, this.traceExceptionStackFirstElement(e));
    }

    /**
     * 印异常栈异常栈第一个元素
     *
     * @param logMessage 日志信息
     * @param stackTraceElement 异常栈元素
     */
    private void logExceptionStackMessage(String logMessage, StackTraceElement stackTraceElement) {
        logger.warn("\n\t\t【Error reason】: " + logMessage
                  + "\n\t\t【Error position】: " + stackTraceElement.getClassName()
                  + " (" + stackTraceElement.getLineNumber() + " row)");
    }

    /**
     * 追踪异常栈第一个元素
     *
     * @param e 异常信息
     * @return StackTraceElement 异常栈元素
     */
    private StackTraceElement traceExceptionStackFirstElement(Exception e) {
        return e.getStackTrace()[0];
    }
}
