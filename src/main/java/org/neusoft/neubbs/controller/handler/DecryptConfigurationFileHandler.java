package org.neusoft.neubbs.controller.handler;

import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.utils.SecretUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 解密配置文件处理器
 *      - 加密内容解析（例：密码）
 *
 * @author Suvan
 */
public class DecryptConfigurationFileHandler extends PropertyPlaceholderConfigurer {
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        //decrypt contain 'password' for property field
        if (propertyName.contains(ParamConst.PASSWORD)) {
            return SecretUtil.decryptBase64(propertyValue);
        }

        return super.convertProperty(propertyName, propertyValue);
    }
}
