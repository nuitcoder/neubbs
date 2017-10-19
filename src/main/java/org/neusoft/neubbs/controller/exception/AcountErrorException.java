package org.neusoft.neubbs.controller.exception;

/**
 * 数据库异常
 *
 * @author Suvan
 */
public class AcountErrorException extends Exception implements IExceptionLog{

   private String logMessage;

    /**
     * Constructor
     */
    public AcountErrorException(String message){
        super(message);
    }
    public AcountErrorException(String message, String logMessage){
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
    public AcountErrorException log(String logMessage){
        this.logMessage = logMessage;
        return this;
    }
}
