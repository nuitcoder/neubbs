package org.neusoft.neubbs.service;

/**
 * 参数检查业务接口
 *
 * @author Suvan
 */
public interface IParamCheckService {

    /**
     * 检查工具
     *      - 使用自定义 RequestParamCheckUtil.java
     *
     * @param paramType 参数类型
     * @param paramValue 参数值
     */
    void check(String paramType, String paramValue);

    /**
     * 检查指令参数，是否存在于指定指令数组
     *
     * @param instructionParam 指令参数(可变参数)
     * @param instructionArray 指令数组（规定指令必须是数组以内元素）
     */
    void checkInstructionOfSpecifyArray(String instructionParam, String... instructionArray);

    /**
     * 参数集合不能为空
     *      - 使用可变参数
     *
     * @param paramType 可变参数
     */
    void paramsNotNull(String... paramType);

    /**
     * 获取 username 参数类型
     *      - 用户 RequestParamCheckUtil.check(paramType, paramValue)
     *      - 支持 name 类型（用户名类型）
     *      - 支持 email 类型（邮箱类型）
     *
     * @param username 用户名
     * @return String 参数类型
     */
    String getUsernameParamType(String username);
}
