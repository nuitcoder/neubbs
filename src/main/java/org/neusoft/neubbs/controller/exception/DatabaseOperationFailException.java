package org.neusoft.neubbs.controller.exception;

/**
 * 数据库错误异常
 *
 * @author Suvan
 */
public class DatabaseOperationFailException extends Exception implements IExceptionLog{

    private String logMessage;

    /**
     * Constructor
     */
    public DatabaseOperationFailException(String message){
        super(message);
    }
    public DatabaseOperationFailException(String message, String logMessage){
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
    public DatabaseOperationFailException log(String logMessage) {
        return this;
    }
}
