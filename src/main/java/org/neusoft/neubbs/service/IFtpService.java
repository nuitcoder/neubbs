package org.neusoft.neubbs.service;

import org.neusoft.neubbs.controller.exception.FtpServiceErrorException;
import org.neusoft.neubbs.entity.UserDO;

/**
 * FTP 服务接口
 *
 * @author Suvan
 */
public interface IFtpService {

    /**
     * 注册用户，新建用户个人目录（FTP 服务器内）
     *
     * @param user 注册的新用户对象
     * @throws FtpServiceErrorException FTP服务错误异常
     */
    void registerUserNewUserPersonDirectory(UserDO user) throws FtpServiceErrorException;
}
