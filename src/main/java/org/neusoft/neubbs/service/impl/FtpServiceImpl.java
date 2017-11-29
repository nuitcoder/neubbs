package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.controller.exception.FtpServiceErrorException;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IFtpService;
import org.neusoft.neubbs.utils.FtpUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * IFtpService 实现类
 *
 * @author Suvan
 */
@Service("ftpServiceImpl")
public class FtpServiceImpl implements IFtpService {

    @Override
    public void registerUserCreatePersonDirectory(UserDO user) throws FtpServiceErrorException {
        try {
            FtpUtil.createDirectory("/user/" + user.getId() + "-" + user.getName() + "/avator");
        } catch (IOException ioe) {
            throw new FtpServiceErrorException(ApiMessage.FTP_SERVICE_EXCEPTION).log(LogWarn.FTP_01);
        }
    }

    @Override
    public void uploadUserAvatorImage(String ftpPath, String fileName, MultipartFile multipartFile) throws FtpServiceErrorException {
        try {
            FtpUtil.uploadFile(ftpPath, fileName, multipartFile.getInputStream());
        } catch (IOException ioe) {
            throw new FtpServiceErrorException(ApiMessage.FTP_SERVICE_EXCEPTION).log(LogWarn.FTP_02);
        }
    }
}
