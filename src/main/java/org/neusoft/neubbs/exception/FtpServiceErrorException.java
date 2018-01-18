package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * Ftp 服务错误异常
 *
 * @author Suvan
 */
@ApiException
public class FtpServiceErrorException extends RuntimeException implements IPrintLog {

    private LogWarnEnum logWarnEnum;

    /**
     * Constructor
     */
    public FtpServiceErrorException(String message) {
        super(message);
    }

    @Override
    public FtpServiceErrorException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
