package org.neusoft.neubbs.service;

/**
 * 验证服务接口
 *      - 参数验证
 *
 * @author Suvan
 */
public interface IValidationService {

    /*
     * ***********************************************
     * param validation
     * ***********************************************
     */

    /**
     * 检查工具
     *      - 使用自定义 RequestParamCheckUtil.java
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
     *      - 格式 Key1, value1, key2, value2, key3, value3 ...
     *      - 若参数不为空，进行参数校验，否则则不校验
     *
     * @param params 可变参数key-value组合集
     */
    void checkNotNullParam(String... params);

    /**
     * 检查参数集不能为空
     *      - 参数集至少有一个不为空的参数，不能全部同时为空
     *
     * @param params 可变参数集
     */
    void checkParamsNotNull(String... params);
}
