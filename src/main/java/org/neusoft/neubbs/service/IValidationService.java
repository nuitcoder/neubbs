package org.neusoft.neubbs.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 验证服务接口
 *      - 参数验证
 *          - 检查工具
 *          - 检查用户名
 *          - 检查命令
 *          - 只检查非空参数
 *          - 检查参数集不能全部为空
 *      - 文件验证
 *          - 检查用户上传头像规范
 *
 * @author Suvan
 */
public interface IValidationService {

    /*
     * ***********************************************
     * param validate
     * ***********************************************
     */

    /**
     * 检查工具
     *      - 使用自定定的 ParamValidateUtil.java
     *      - 支持链式调用
     *
     * @param paramType 参数类型
     * @param paramValue 参数值
     * @return IValidationService 参数检查服务接口（链式调用）
     */
    IValidationService check(String paramType, String paramValue);

    /**
     * 检查用户名
     *      - 支持 name 类型
     *      - 支持 email 类型
     *      - 支持链式调用
     *
     * @param username 用户名
     * @return IValidationService 参数检查服务接口（链式调用）
     */
     IValidationService checkUsername(String username);

    /**
     * 检查命令
     *      - 检查输入参数，是否属于指定指令数组之一
     *
     * @param command 输入指令
     * @param commandArray 指令数组（规定指令必须是数组以内元素）
     */
    void checkCommand(String command, String... commandArray);

    /**
     * 只检查非空参数
     *      - 统一类型 String
     *      - 【必须符合格式】格式 Key1, value1, key2, value2, key3, value3 ...
     *      - 若参数为 null，则不进行参数校验
     *
     * @param params 可变参数 key-value 组合集
     */
    void checkOnlyNotNullParam(String... params);

    /**
     * 检查参数集不能全部为空
     *      - 参数集内至少有一个不为空（不能全部为空）
     *
     * @param params 可变参数集
     */
    void checkParamsNotAllNull(String... params);


    /*
     * ***********************************************
     * file validate
     * ***********************************************
     */

    /**
     * 检查上传头像规范（图片）
     *      - 是否为空
     *      - 是否匹配指定类型
     *      - 是否超过指定大小
     *
     * @param avatarFile 头像文件对象
     */
    void checkUserUploadAvatarNorm(MultipartFile avatarFile);
}
