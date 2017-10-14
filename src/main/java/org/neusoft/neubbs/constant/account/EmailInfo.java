package org.neusoft.neubbs.constant.account;

/**
 * 邮件信息
 */
public interface EmailInfo {
    //账户激活邮件
    String EMAIL_ACTIVATION_FROM_SUBJECT = "Neubbs 账户激活";
    String EMAIL_AUTIVATION_SEND_EMAIL_SUCCESS = "已成功发送账户激活链接，到指定用户邮箱，有效期 24小时！";
    String EMAIL_AUTIVATION_SEND_EMAIL_FAIL = "服务器发送邮件失败！";
    String EMAIL_NO_REGISTER_NO_SEND_EMAIL = "该邮箱未注册，无法发送邮件";
}
