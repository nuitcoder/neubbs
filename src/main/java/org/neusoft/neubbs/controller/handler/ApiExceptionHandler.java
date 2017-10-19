package org.neusoft.neubbs.controller.handler;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.controller.exception.AcountErrorException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TokenExpireException;
import org.neusoft.neubbs.util.ResponsePrintWriterUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api 异常处理器（继承 Spring 异常处理解析器）
 *
 * @author suvan
 */
public class ApiExceptionHandler implements HandlerExceptionResolver{

    private static final Logger logger = Logger.getLogger(ApiExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        //api 页面输出错误信息
        ResponsePrintWriterUtils.outFailJSONMessage(response, e.getMessage());

        //根据不同异常打印日志
        if(e instanceof ParamsErrorException){
            logger.warn(((ParamsErrorException) e).getLogMessage());
        } else if (e instanceof AcountErrorException) {
            logger.warn(((AcountErrorException) e).getLogMessage());
        } else if (e instanceof TokenExpireException) {
            logger.warn(((TokenExpireException) e).getLogMessage());
        }

        return null;
    }
}
