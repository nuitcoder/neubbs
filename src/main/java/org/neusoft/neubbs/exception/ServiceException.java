package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 业务异常
 *      - 仅用在业务层
 *
 * @author Suvan
 */
@ApiException
public class ServiceException extends RuntimeException implements IPrintLog {

   private LogWarnEnum logWarnEnum;

    /**
     * Constructor
     */
    public ServiceException(String message) {
        super(message);
    }

    @Override
    public ServiceException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
