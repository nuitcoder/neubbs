package org.neusoft.neubbs.controller.handler;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.controller.annotation.ApiException;
import org.neusoft.neubbs.exception.IPrintLog;
import org.neusoft.neubbs.utils.AnnotationUtil;
import org.neusoft.neubbs.utils.ResponsePrintWriterUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api 异常处理器
 *      -  继承 Spring 异常处理解析器，异常抛出时调用，经由此类，统一处理整个项目所抛出的异常,
 *
 * @author suvan
 */
public class ApiExceptionHandler implements HandlerExceptionResolver {

    private Logger logger = Logger.getRootLogger();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object o, Exception e) {
        //Exception had to statement @ApiException
        if (AnnotationUtil.hasClassAnnotation(e.getClass(), ApiException.class)) {
            ResponsePrintWriterUtil.outFailJSONMessage(response, e.getMessage());
        }

        //print local log
        if (e instanceof IPrintLog) {
            String logMessage = ((IPrintLog) e).getLogMessage();
            if (logMessage != null) {
                //get exception stack root element (pass stacktrace)
                StackTraceElement stackElement = e.getStackTrace()[0];

                logger.warn("\n\t\t【Error reason】: " + logMessage
                          + "\n\t\t【Error position】: " + stackElement.getClassName()
                          + " (" + stackElement.getLineNumber() + " row)"
                );
            }
        }

        return null;
    }
}
