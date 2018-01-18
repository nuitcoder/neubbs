package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 数据库异常
 *      1.声明 @ApiException ，表名被 ApiExceptinHandler处理
 *
 * @author Suvan
 */
@ApiException
public class AccountErrorException extends RuntimeException implements IPrintLog {

   private LogWarnEnum logWarnEnum;

    /**
     * Constructor
     */
    public AccountErrorException(String message) {
        super(message);
    }

    @Override
    public AccountErrorException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
