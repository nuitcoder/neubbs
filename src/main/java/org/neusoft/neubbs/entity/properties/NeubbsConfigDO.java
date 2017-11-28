package org.neusoft.neubbs.entity.properties;

import org.neusoft.neubbs.constant.api.SetConst;

/**
 * 配置文件 neubbs.properties 实体类
 *
 * @author Suvan
 */
public class NeubbsConfigDO {

    private Integer cookieAutoLoginMaxAgeDay;
    private Integer topicsApiRequestParamLimitDefault;
    private String userImageUploadPath;
    private String accountApiVaslidateUrl;
    private String userFtpUrl;

    public Integer getCookieAutoLoginMaxAgeDay() {
        //day -> second
        return cookieAutoLoginMaxAgeDay * SetConst.TWENTY_FOUR * SetConst.SIXTY * SetConst.SIXTY;
    }

    public void setCookieAutoLoginMaxAgeDay(Integer cookieAutoLoginMaxAgeDay) {
        this.cookieAutoLoginMaxAgeDay = cookieAutoLoginMaxAgeDay;
    }

    public Integer getTopicsApiRequestParamLimitDefault() {
        return topicsApiRequestParamLimitDefault;
    }

    public void setTopicsApiRequestParamLimitDefault(Integer topicsApiRequestParamLimitDefault) {
        this.topicsApiRequestParamLimitDefault = topicsApiRequestParamLimitDefault;
    }

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

    public String getUserFtpUrl() {
        return userFtpUrl;
    }

    public void setUserFtpUrl(String userFtpUrl) {
        this.userFtpUrl = userFtpUrl;
    }

}
