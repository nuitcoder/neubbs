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
     * 检查 2 个参数不能为空的 3 种情况（执行相应参数检查）
     *      - 参数 1 和 参数 2 都不为空
     *      - 参数 1 不为空，参数 2 为空
     *      - 参数 1 为空，参数 2不为空
     *
     *      -【/topics 接口】 category 和 username
     *
     * @param paramType1 参数类型1
     * @param paramValue1 参数值1
     * @param paramType2 参数类型2
     * @param paramValue2 参数类型2
     */
    void checkTwoParamNotNulThreeCondition(String paramType1, String paramValue1,
                                           String paramType2, String paramValue2);

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
