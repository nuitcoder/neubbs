package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 工具类异常
 *      - 仅用在工具层
 *
 * @author Suvan
 */
@ApiException
public class UtilClassException extends RuntimeException implements IPrintLog {

    private LogWarnEnum logWarnEnum;

    /**
     * Constructor
     */
    public UtilClassException(String message) {
        super(message);
    }

    @Override
    public UtilClassException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
