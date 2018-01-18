package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;

/**
 * 数据库操作失败异常
 *
 * @author Suvan
 */
public class DatabaseOperationFailException extends RuntimeException implements IPrintLog {

    private LogWarnEnum logWarnEnum;

    /**
     * Constructor
     */
    public DatabaseOperationFailException(String message) {
        super(message);
    }

    @Override
    public DatabaseOperationFailException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
