package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.controller.exception.FileUploadErrorException;
import org.neusoft.neubbs.service.IFileTreatService;
import org.neusoft.neubbs.utils.PatternUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * IFileTreatServer 接口实现类
 *
 * @author Suvan
 */
@Service("fileTreatServiceImpl")
public class FileTreatServiceImpl implements IFileTreatService {

    @Override
    public void checkUploadUserAvatorImageFileNorm(MultipartFile userImageFile) {
        //no empty
        if (userImageFile.isEmpty()) {
            throw new FileUploadErrorException(ApiMessage.NO_CHOICE_PICTURE).log(LogWarn.FILE_01);
        }

        //format match
        String fileType = userImageFile.getContentType();
        if (!PatternUtil.matchUserImage(fileType)) {
            throw new FileUploadErrorException(ApiMessage.PICTURE_FORMAT_WRONG).log(fileType + LogWarn.FILE_02);
        }

        //no exceed 5MB
        if (userImageFile.getSize() >  SetConst.SIZE_FIVE_MB) {
            throw new FileUploadErrorException(ApiMessage.PICTURE_TOO_LARGE).log(LogWarn.FILE_05);
        }
    }


    @Override
    public void transferToServer(MultipartFile multipartFile, String serverDirectoryPath, String serverFileName) {
        File imageFile = new File(serverDirectoryPath, serverFileName);
        if (!imageFile.getParentFile().exists()) {
            throw new FileUploadErrorException(ApiMessage.NO_PARENT_DIRECTORY).log(LogWarn.FILE_03);
        }

        try {
            multipartFile.transferTo(imageFile);
        } catch (IOException e) {
            throw new FileUploadErrorException(ApiMessage.UPLOAD_FAIL).log(LogWarn.FILE_04);
        }
    }

    @Deprecated
    @Override
    public MultipartFile compressFile(MultipartFile multipartFile) {
        return null;
    }
}
