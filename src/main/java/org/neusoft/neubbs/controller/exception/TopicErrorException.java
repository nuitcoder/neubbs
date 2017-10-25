package org.neusoft.neubbs.controller.exception;

import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 话题异常
 *
 * @author Suvan
 */
@ApiException
public class TopicErrorException extends Exception implements IExceptionLog{

    private String logMessage;

    /**
     * Constructor
     */
    public TopicErrorException(String message){
        super(message);
    }

    /**
     * Getter
     */
    public String getLogMessage(){
        return logMessage;
    }

    @Override
    public TopicErrorException log(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }
}
