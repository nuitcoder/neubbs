package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * token 过期异常
 *
 * @author Suvan
 */
@ApiException
public class TokenErrorException extends RuntimeException implements IPrintLog {

    private LogWarnEnum logWarnEnum;

    /**
     * Constructor
     */
    public TokenErrorException(String message) {
        super(message);
    }

    @Override
    public TokenErrorException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
