package org.neusoft.neubbs.constant.api;

/**
 * 文件信息
 *
 * @author Suvan
 */
public interface FileInfo {
    /**
     * File api 警告信息
     */
    String NO_CHOICE_PICTURE = "no choice to upload picture";
    String PICTURE_FORMAT_WRONG = "user upload picture type is wrong, only *.jpg or *.png or *.jpeg";
    String NO_PARENT_DIRECTORY = "there is no parent directory";
    String UPLOAD_SUCCESS = "upload success";

    /**
     * 路径
     */
    String UPLOAD_USER_IMAGE_PATH = "/WEB-INF/file/user/image/";

    /**
     * 文件大小
     */
    long SIZE_ONE_MB = 1048576;


}
