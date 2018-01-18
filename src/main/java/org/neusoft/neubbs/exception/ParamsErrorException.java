package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 参数异常
 *
 * @author Suvan
 */
@ApiException
public class ParamsErrorException extends RuntimeException implements IPrintLog {

    /**
     * 日志信息
     */
    private LogWarnEnum logWarnEnum;

    /**
     *  Constructor
     */
    public ParamsErrorException(String message) {
        super(message);
    }

    @Override
    public ParamsErrorException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
