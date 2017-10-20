package org.neusoft.neubbs.controller.exception;

import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 参数异常
 *
 * @author Suvan
 */
@ApiException
public class ParamsErrorException extends Exception implements IExceptionLog {

    /**
     * 日志信息
     */
    private String logMessage;

    /**
     *  Constructor
     */
    public ParamsErrorException(String message){
        super(message);
    }
    public ParamsErrorException(String message, String logMessage) {
       super(message);
       this.logMessage = logMessage;
    }

    /**
     * Getter
     */
    public String getLogMessage(){
        return logMessage;
    }

    @Override
    public ParamsErrorException log(String logMessage){
        this.logMessage = logMessage;
        return this;
    }
}
