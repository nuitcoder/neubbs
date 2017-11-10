package org.neusoft.neubbs.controller.exception;

import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * token 过期异常
 *
 * @author Suvan
 */
@ApiException
public class TokenErrorException extends Exception implements IPrintLog {

    private String logMessage;

    /**
     * Constructor
     */
    public TokenErrorException(String message) {
        super(message);
    }

    @Override
    public TokenErrorException log(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }
}
