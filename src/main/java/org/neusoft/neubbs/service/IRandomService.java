package org.neusoft.neubbs.service;

/**
 * 随机数业务接口
 *
 * @author Suvan
 */
public interface IRandomService {

    /**
     * 生成 6 位数随机密码
     *
     * @return String 6位数随机密码文本
     */
    String generateSixDigitsRandomPassword();
}
