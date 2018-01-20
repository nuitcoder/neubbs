package org.neusoft.neubbs.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理业务接口
 *
 * @author Suvan
 */
public interface IFileTreatService {
    /**
     * 检查上传头像规范（图片）
     *      - 是否为空
     *      - 是否匹配指定类型
     *      - 是否超过指定大小
     *
     * @param avatarFile 头像文件对象
     */
    void checkUploadAvatarNorm(MultipartFile avatarFile);
}
