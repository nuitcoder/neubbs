package org.neusoft.neubbs.controller.exception;

import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 数据库异常
 *      1.声明 @ApiException ，表名被 ApiExceptinHandler处理
 *
 * @author Suvan
 */
@ApiException
public class AccountErrorException extends Exception implements IExceptionLog{

   private String logMessage;

    /**
     * Constructor
     */
    public AccountErrorException(String message){
        super(message);
    }

    /**
     * Getter
     */
    public String getLogMessage(){
        return logMessage;
    }

    @Override
    public AccountErrorException log(String logMessage){
        this.logMessage = logMessage;
        return this;
    }
}
