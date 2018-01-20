package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.FileUploadErrorException;
import org.neusoft.neubbs.service.IFileTreatService;
import org.neusoft.neubbs.utils.PatternUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * IFileTreatServer 接口实现类
 *
 * @author Suvan
 */
@Service("fileTreatServiceImpl")
public class FileTreatServiceImpl implements IFileTreatService {

    @Override
    public void checkUploadAvatarNorm(MultipartFile avatarFile) {
        //no empty
        if (avatarFile.isEmpty()) {
            throw new FileUploadErrorException(ApiMessage.NO_CHOICE_PICTURE).log(LogWarnEnum.FS1);
        }

        //match file type(.xxx)
        if (!PatternUtil.matchUserImage(avatarFile.getContentType())) {
            throw new FileUploadErrorException(ApiMessage.PICTURE_FORMAT_WRONG).log(LogWarnEnum.FS2);
        }

        //no exceed 5MB
        if (avatarFile.getSize() >  SetConst.USER_AVATOR_MAX_SIZE_FIVE_MB) {
            throw new FileUploadErrorException(ApiMessage.PICTURE_TOO_LARGE).log(LogWarnEnum.FS5);
        }
    }
}
