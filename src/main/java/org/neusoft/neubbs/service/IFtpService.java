package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;
import org.springframework.web.multipart.MultipartFile;

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
     */
    void registerUserCreatePersonDirectory(UserDO user);

    /**
     * 上传用户头像图片，至 FTP 服务器
     *
     * @param serverDirectoryPath 服务器目录路径
     * @param serverFileName 服务器文件名
     * @param multipartFile 用户上传文件对象
     */
    void uploadUserAvatorImage(String serverDirectoryPath, String serverFileName, MultipartFile multipartFile);
}
