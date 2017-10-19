package org.neusoft.neubbs.controller.exception;

/**
 * token 过期异常
 *
 * @author Suvan
 */
public class TokenExpireException extends Exception implements IExceptionLog{

    private String logMessage;

    /**
     * Constructor
     */
    public TokenExpireException(String message){
        super(message);
    }
    public TokenExpireException(String message, String logMessage){
        super(message);
        this.logMessage = message;
    }

    /**
     * Getter
     */
    public String getLogMessage(){
        return logMessage;
    }

    @Override
    public TokenExpireException log(String logMessage){
        this.logMessage = logMessage;
        return this;
    }
}
