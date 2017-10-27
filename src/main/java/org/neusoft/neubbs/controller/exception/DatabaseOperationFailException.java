package org.neusoft.neubbs.controller.exception;

/**
 * 数据库操作失败异常
 *
 * @author Suvan
 */
public class DatabaseOperationFailException extends Exception implements IExceptionLog {

    private String logMessage;

    /**
     * Constructor
     */
    public DatabaseOperationFailException(String message) {
        super(message);
    }

    /**
     * Getter
     */
    public String getLogMessage() {
        return logMessage;
    }

    @Override
    public DatabaseOperationFailException log(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }
}
