package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 文件上传错误异常
 *
 * @author Suvan
 */
@ApiException
public class FileUploadErrorException extends RuntimeException implements IPrintLog {

    private LogWarnEnum logWarnEnum;

    /**
     * Constructor
     */
    public FileUploadErrorException(String message) {
        super(message);
    }

    @Override
    public FileUploadErrorException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
