package org.neusoft.neubbs.constant.api;

/**
 * 文件信息
 *
 * @author Suvan
 */
public final class FileInfo {
    private FileInfo() { }

    /**
     * File api 警告信息
     */
    public static final String NO_CHOICE_PICTURE = "no choice to upload picture";
    public static final String PICTURE_FORMAT_WRONG = "user upload picture type is wrong,"
                                                       + " only *.jpg or *.png or *.jpeg";
    public static final String NO_PARENT_DIRECTORY = "there is no parent directory";
    public static final String UPLOAD_SUCCESS = "upload success";

    /**
     * 路径
     */
    public static final String UPLOAD_USER_IMAGE_PATH = "/WEB-INF/file/user/image/";

    /**
     * 文件大小
     */
    public static final long SIZE_ONE_MB = 1048576;


}
