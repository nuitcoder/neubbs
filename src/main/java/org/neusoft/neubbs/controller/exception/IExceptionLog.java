package org.neusoft.neubbs.controller.exception;

/**
 * 异常日志接口
 *
 * @author Suvan
 */
public interface IExceptionLog {

    /**
     * 获取日志信息
     *
     * @param logMessage
     * @return this
     */
    Object log(String logMessage);
}
