package org.neusoft.neubbs.controller.handler;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.controller.annotation.ApiException;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.FileUploadException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TokenExpireException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
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

    private static final Logger LOGGER = Logger.getLogger(ApiExceptionHandler.class);

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
        //api 页面输出错误信息（只输出 Api 指定异常）
        if (AnnotationUtil.hasClassAnnotation(e.getClass(), ApiException.class)) {
            ResponsePrintWriterUtil.outFailJSONMessage(response, e.getMessage());
        }

        //匹配自定义异常，打印日志
        if (e instanceof ParamsErrorException) {
            LOGGER.warn(((ParamsErrorException) e).getLogMessage());
        } else if (e instanceof AccountErrorException) {
            LOGGER.warn(((AccountErrorException) e).getLogMessage());
        } else if (e instanceof TokenExpireException) {
            LOGGER.warn(((TokenExpireException) e).getLogMessage());
        } else if (e instanceof FileUploadException) {
            LOGGER.warn(((FileUploadException) e).getLogMessage());
        } else if (e instanceof TopicErrorException) {
            LOGGER.warn(((TopicErrorException) e).getLogMessage());
        } else if (e instanceof DatabaseOperationFailException) {
            LOGGER.warn(((DatabaseOperationFailException) e).getLogMessage());
        }

        //未处理异常，页面报错
        return null;
    }
}
