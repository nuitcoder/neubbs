package org.neusoft.neubbs.controller.handler;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.controller.annotation.ApiException;
import org.neusoft.neubbs.controller.exception.*;
import org.neusoft.neubbs.util.AnnotationUtils;
import org.neusoft.neubbs.util.ResponsePrintWriterUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api 异常处理器（继承 Spring 异常处理解析器，处理整个项目所抛出的异常）
 *
 * @author suvan
 */
public class ApiExceptionHandler implements HandlerExceptionResolver{

    private static final Logger logger = Logger.getLogger(ApiExceptionHandler.class);

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
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        //api 页面输出错误信息（只输出 Api 指定异常）
        if (AnnotationUtils.hasClassAnnotation(e.getClass(), ApiException.class)) {
            ResponsePrintWriterUtils.outFailJSONMessage(response, e.getMessage());
        }

        //根据不同异常打印日志
        if(e instanceof ParamsErrorException){
            logger.warn(((ParamsErrorException) e).getLogMessage());
        } else if (e instanceof AccountErrorException) {
            logger.warn(((AccountErrorException) e).getLogMessage());
        } else if (e instanceof TokenExpireException) {
            logger.warn(((TokenExpireException) e).getLogMessage());
        } else if (e instanceof FileUploadException) {
            logger.warn(((FileUploadException) e).getLogMessage());
        } else if (e instanceof TopicErrorException) {
            logger.warn(((TopicErrorException) e).getLogMessage());
        }

        return null;
    }
}
