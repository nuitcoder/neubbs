package org.neusoft.neubbs.controller.handler;

import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.utils.SecretUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 解密读取配置文件信息处理器
 *
 * @author Suvan
 */
public class DecryptPropertyPlaceholderConfigurerHandler extends PropertyPlaceholderConfigurer {
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        //decrypt contain 'password' for property field
        if (propertyName.contains(ParamConst.PASSWORD)) {
            return SecretUtil.decryptBase64(propertyValue);
        }

        return super.convertProperty(propertyName, propertyValue);
    }
}
