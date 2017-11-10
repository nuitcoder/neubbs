package org.neusoft.neubbs.controller.exception;

/**
 * 打印日志接口
 *
 * @author Suvan
 */
public interface IPrintLog {

    /**
     * 储存日志信息
     *
     * @param logMessage 日志信息
     * @return this
     */
    IPrintLog log(String logMessage);

    /**
     * 获取日志信息
     *
     * @return String 获取日志信息
     */
    String getLogMessage();
}
