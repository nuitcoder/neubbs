package org.neusoft.neubbs.service;

import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.FileUploadErrorException;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理业务接口
 *
 * @author Suvan
 */
public interface IFileService {
    /**
     * 检查上传用户头像文件规范
     *
     * @param userImageFile 用户头像文件
     * @throws FileUploadErrorException 文件上传异常
     */
    void checkUserImageNorm(MultipartFile userImageFile) throws FileUploadErrorException;

    /**
     * 构建服务器端保存用户图片，文件名
     *
     * @param userId 用户id
     * @param uploadFileName 上传文件名
     * @return String 服务器端文件名
     * @throws AccountErrorException 账户错误异常
     */
    String buildServerKeppUserImageFileName(int userId, String uploadFileName) throws AccountErrorException;

    /**
     * 传输文件至服务器
     *
     * @param multipartFile 上传文件对象
     * @param serverSaveUserImageFilePath 服务器保存用户头像文件路径
     * @param fileName 文件名
     * @throws FileUploadErrorException 文件上传错误异常
     */
    void transferToServer(MultipartFile multipartFile, String serverSaveUserImageFilePath, String fileName)
            throws FileUploadErrorException;

    /**
     * 压缩文件
     *
     * @param multipartFile 上传文件
     * @return MultipartFile 压缩后新文件
     */
    MultipartFile compressFile(MultipartFile multipartFile);
}
