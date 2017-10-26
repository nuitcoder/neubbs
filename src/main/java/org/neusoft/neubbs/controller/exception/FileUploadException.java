package org.neusoft.neubbs.controller.exception;

import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 文件上传错误异常
 *
 * @author Suvan
 */
@ApiException
public class FileUploadException extends Exception implements IExceptionLog {

    private String logMessage;

    /**
     * Constructor
     */
    public FileUploadException(String message) {
        super(message);
    }

    /**
     * Getter
     */
    public String getLogMessage() {
        return logMessage;
    }

    @Override
    public FileUploadException log(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }
}
