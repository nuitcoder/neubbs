package org.neusoft.neubbs.controller.exception;

/**
 * 数据库错误异常
 *
 * @author Suvan
 */
public class DatabaseErrorException extends Exception implements IExceptionLog{

    private String logMessage;

    /**
     * Constructor
     */
    public DatabaseErrorException(String message){
        super(message);
    }
    public DatabaseErrorException(String message, String logMessage){
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
    public DatabaseErrorException log(String logMessage) {
        return this;
    }
}
