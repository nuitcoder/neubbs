package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;
import org.springframework.web.multipart.MultipartFile;

/**
 * FTP 服务接口
 *      - FTP 服务器上传头像
 *
 * @author Suvan
 */
public interface IFtpService {

    /**
     * 创建用户个人目录
     *      - ftp 服务器上私人目录
     *          - 包含头像目录
     *
     * @param user 注册的新用户对象
     */
    void createUserPersonalDirectory(UserDO user);

    /**
     * 上传用户头像
     *
     * @param user 用户对象
     * @param avatarFile 头像文件对象
     */
    void uploadUserAvatar(UserDO user, MultipartFile avatarFile);

    /**
     * 生成服务器头像文件名
     *
     * @param avatarFile 头像文件对象
     * @return String 服务器头像文件名
     */
    String generateServerAvatarFileName(MultipartFile avatarFile);
}
