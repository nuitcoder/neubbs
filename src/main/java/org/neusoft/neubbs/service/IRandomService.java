package org.neusoft.neubbs.service;

/**
 * 随机数业务接口
 *      - 生成 6 位数随机密码
 *
 * @author Suvan
 */
public interface IRandomService {

    /**
     * 生成 6 位数随机密码
     *      - 用于用户忘记密码后，修改为临时密码，并发送提示邮件
     *
     * @return String 6 位数随机密码文本
     */
    String generateSixDigitsRandomPassword();
}
