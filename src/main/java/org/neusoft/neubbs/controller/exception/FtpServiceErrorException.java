package org.neusoft.neubbs.controller.exception;

import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * Ftp 服务错误异常
 *
 * @author Suvan
 */
@ApiException
public class FtpServiceErrorException extends RuntimeException implements IPrintLog {

    private String logMessage;

    /**
     * Constructor
     */
    public FtpServiceErrorException(String message) {
        super(message);
    }

    @Override
    public FtpServiceErrorException log(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }
}
