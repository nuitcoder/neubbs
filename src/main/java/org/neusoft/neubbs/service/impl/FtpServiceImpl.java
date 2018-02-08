package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.ServiceException;
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
    public void createUserPersonalDirectory(UserDO user) {
        try {
            String userPath = "/user/" + user.getId() + "-" + user.getName();
            FtpUtil.createDirectory(userPath);
            FtpUtil.createDirectory(userPath + "/avatar");
        } catch (IOException ioe) {
            throw new ServiceException(ApiMessage.FTP_SERVICE_EXCEPTION).log(LogWarnEnum.FTPS1);
        }
    }

    @Override
    public void uploadUserAvatar(UserDO user, MultipartFile avatarFile) {
        try {
            String serverUploadPath = "/user/" + user.getId() + "-" + user.getName() + "/avatar/";
            String serverFileName = this.generateServerAvatarFileName(avatarFile);

            FtpUtil.uploadFile(serverUploadPath, serverFileName, avatarFile.getInputStream());
        } catch (IOException ioe) {
            throw new ServiceException(ApiMessage.FTP_SERVICE_EXCEPTION).log(LogWarnEnum.FTPS2);
        }
    }

    @Override
    public String generateServerAvatarFileName(MultipartFile avatarFile) {
        return System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
    }
}
