package org.neusoft.neubbs.controller.exception;

/**
 * 异常日志接口
 *
 * @author Suvan
 */
public interface IExceptionLog {

    /**
     * 储存日志信息
     *
     * @param logMessage 日志信息
     * @return this
     */
    Object log(String logMessage);
}
