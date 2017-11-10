package org.neusoft.neubbs.controller.exception;

/**
 * 数据库操作失败异常
 *
 * @author Suvan
 */
public class DatabaseOperationFailException extends Exception implements IPrintLog {

    private String logMessage;

    /**
     * Constructor
     */
    public DatabaseOperationFailException(String message) {
        super(message);
    }

    @Override
    public DatabaseOperationFailException log(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }
}
