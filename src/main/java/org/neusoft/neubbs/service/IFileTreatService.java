package org.neusoft.neubbs.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理业务接口
 *
 * @author Suvan
 */
public interface IFileTreatService {
    /**
     * 检查上传用户头像文件规范
     *
     * @param userImageFile 用户头像文件
     */
    void checkUploadUserAvatorImageFileNorm(MultipartFile userImageFile);


    /**
     * 传输文件至服务器
     *
     * @param multipartFile 上传文件对象
     * @param serverDirectoryPath 服务器目录路径
     * @param serverFileName 服务器文件名
     */
    void transferToServer(MultipartFile multipartFile, String serverDirectoryPath, String serverFileName);

    /**
     * 压缩文件
     *
     * @param multipartFile 上传文件
     * @return MultipartFile 压缩后新文件
     */
    MultipartFile compressFile(MultipartFile multipartFile);
}
