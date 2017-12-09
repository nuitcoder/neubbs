package org.neusoft.neubbs.controller.handler;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.controller.annotation.ApiException;
import org.neusoft.neubbs.controller.exception.IPrintLog;
import org.neusoft.neubbs.utils.AnnotationUtil;
import org.neusoft.neubbs.utils.ResponsePrintWriterUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api 异常处理器（继承 Spring 异常处理解析器，处理整个项目所抛出的异常）
 *
 * @author suvan
 */
public class ApiExceptionHandler implements HandlerExceptionResolver {

    private Logger logger = Logger.getRootLogger();

    /**
     * 解析异常
     *
     * @param request http请求
     * @param response http响应
     * @param o 抛出异常的方法对象
     * @param e 异常对象
     * @return ModelAndView 视图对象
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object o, Exception e) {
        //异常存在 @ApiException 注解
        if (AnnotationUtil.hasClassAnnotation(e.getClass(), ApiException.class)) {
            //页面输出报错信息
            ResponsePrintWriterUtil.outFailJSONMessage(response, e.getMessage());
        }

        //打印日志信息（已实现 IPrintLog 接口，存在日志信息）
        if (e instanceof IPrintLog) {
            String logMessage = ((IPrintLog) e).getLogMessage();
            if (logMessage != null) {
                //starck root exception element (stacktrace - 堆栈跟踪)
                StackTraceElement sree = e.getStackTrace()[0];

                logger.warn("\n\t\t【Error reason】: " + logMessage
                        + "\n\t\t【Error position】: " + sree.getClassName() + " (" + sree.getLineNumber() + " row)"
                );
            }
        }

        return null;
    }
}
