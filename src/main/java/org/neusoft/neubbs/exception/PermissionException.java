package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 权限异常
 *      - 用于权限管理
 *      - api 拦截器
 *
 * @author Suvan
 */
@ApiException
public class PermissionException extends RuntimeException implements IPrintLog {

   private LogWarnEnum logWarnEnum;

    /**
     * Constructor
     */
    public PermissionException(String message) {
        super(message);
    }

    @Override
    public PermissionException log(LogWarnEnum logWarnEnum) {
        this.logWarnEnum = logWarnEnum;
        return this;
    }

    @Override
    public LogWarnEnum getLog() {
        return logWarnEnum;
    }
}
