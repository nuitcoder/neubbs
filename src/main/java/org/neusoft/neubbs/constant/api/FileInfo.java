package org.neusoft.neubbs.constant.api;

/**
 * 文件信息
 *
 * @author Suvan
 */
public interface FileInfo {

    long SIZE_ONE_MB = 1048576;

    /**
     * File api 警告信息
     */
    String UPLOAD_FAIL = "upload failed!";
    String UPLOAD_SUCCESS = "upload successful!";
    String NO_CHOICE_PICTURE = "no choice to upload picture";
    String PICTURE_FORMAT_WRONG = "user upload picture type is wrong, only *.jpg or *.png or *.jpeg";

}
