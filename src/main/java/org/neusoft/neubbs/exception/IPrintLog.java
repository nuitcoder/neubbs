package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;

/**
 * 打印日志接口
 *      - neubbs 项目自定义的异常，请务必实现该接口，统一日志记录
 *
 * @author Suvan
 */
public interface IPrintLog {

    /**
     * 储存日志信息
     *
     * @param logWarnEnum 日志警告信息枚举实例
     * @return this 当前对象（实现该接口的类对象）
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
