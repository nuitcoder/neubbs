package org.neusoft.neubbs.controller.exception;

import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * token 过期异常
 *
 * @author Suvan
 */
@ApiException
public class TokenExpireException extends Exception implements IExceptionLog {

    private String logMessage;

    /**
     * Constructor
     */
    public TokenExpireException(String message) {
        super(message);
    }

    /**
     * Getter
     */
    public String getLogMessage() {
        return logMessage;
    }

    @Override
    public TokenExpireException log(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }
}
