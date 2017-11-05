package org.neusoft.neubbs.entity.properties;

/**
 * 配置文件 neubbs.properties 实体类
 *
 * @author Suvan
 */
public class NeubbsConfigDO {

    private String userImageUploadPath;

    private String accountApiVaslidateUrl;

    public String getUserImageUploadPath() {
        return userImageUploadPath;
    }

    public void setUserImageUploadPath(String userImageUploadPath) {
        this.userImageUploadPath = userImageUploadPath;
    }

    public String getAccountApiVaslidateUrl() {
        return accountApiVaslidateUrl;
    }

    public void setAccountApiVaslidateUrl(String accountApiVaslidateUrl) {
        this.accountApiVaslidateUrl = accountApiVaslidateUrl;
    }
}
