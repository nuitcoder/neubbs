package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;

/**
 * 打印日志接口
 *      - 自定义异常，必须实现该 log 接口的函数
 *
 * @author Suvan
 */
public interface IPrintLog {

    /**
     * 储存日志信息
     *
     * @param logWarnEnum 日志警告信息枚举实例
     * @return this
     */
    IPrintLog log(LogWarnEnum logWarnEnum);

    /**
     * 获取日志心里实例
     *      - 子类实现，从 LogWarnEnum.getErrorMessage() 获取
     *
     * @return LogWarnEnum 日志警告信息枚举类实例
     */
    LogWarnEnum getLog();
}
