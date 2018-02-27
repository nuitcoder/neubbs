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
 * @author suvan
 */
public class ApiExceptionHandler implements HandlerExceptionResolver {

    private Logger logger = Logger.getRootLogger();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object o, Exception e) {

        //Exception had to statement @ApiException
        if (e.getClass().getAnnotation(ApiException.class) != null) {
            this.outFailJsonMessage(response, e.getMessage());
            this.printApiExceptionToLocalLog(e);
        } else if (e instanceof ClassCastException) {
            this.outFailJsonMessage(response, ApiMessage.PARAM_ERROR);
            logger.warn("访问 " + request.getRequestURI() + " 接口，传入参数类型错误！请参考《后端 API 交互协议》文档！");
        } else {
            //output unknown error
            this.outFailJsonMessage(response, ApiMessage.UNKNOWN_ERROR);
            logger.warn("服务端出现我未知错误, 已邮件通知开发人员开发人员尽快处理！");
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
     * 打印 Api 异常到本地日志
     *
     * @param e 异常对象
     */
    private void printApiExceptionToLocalLog(Exception e) {
        //print api exception to local log
        if (e instanceof IPrintLog) {

            LogWarnEnum logWarnEnum = ((IPrintLog) e).getLog();
            String logMessage = logWarnEnum != null ? logWarnEnum.getErrorMessage() : null;

            if (logMessage != null) {
                //get exception stack root element (pass stacktrace)
                StackTraceElement stackElement = e.getStackTrace()[0];

                logger.warn("\n\t\t【Error reason】: " + logMessage
                          + "\n\t\t【Error position】: " + stackElement.getClassName()
                          + " (" + stackElement.getLineNumber() + " row)"
                );
            }
        }
    }
}
