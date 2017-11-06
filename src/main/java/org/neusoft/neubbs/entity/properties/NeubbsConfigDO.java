package org.neusoft.neubbs.entity.properties;

import org.neusoft.neubbs.constant.api.SetConst;

/**
 * 配置文件 neubbs.properties 实体类
 *
 * @author Suvan
 */
public class NeubbsConfigDO {

    private Integer cookieAutoLoginMaxAgeDay;

    public Integer getCookieAutoLoginMaxAgeDay() {
        //day -> second
        return cookieAutoLoginMaxAgeDay * SetConst.TWENTY_FOUR * SetConst.SIXTY * SetConst.SIXTY;
    }

    public void setCookieAutoLoginMaxAgeDay(Integer cookieAutoLoginMaxAgeDay) {
        this.cookieAutoLoginMaxAgeDay = cookieAutoLoginMaxAgeDay;
    }

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
