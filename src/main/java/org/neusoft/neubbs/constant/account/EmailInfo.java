package org.neusoft.neubbs.constant.account;

/**
 * 邮件信息
 */
public interface EmailInfo {
    //成功
    String ACCOUTN_ACTIVATE_EMAIL_SEND_SUCCESS = "账户激活邮件，已成功发送到指定用户邮箱，有效期 24小时！";

    String ENGLISH_ACCOUTN_ACTIVATE_EMAIL_SEND_SUCCESS = "Account Activate Email Send Success!";

    //账户激活邮件
    String EMAIL_ACTIVATE_FROM_SUBJECT = "Neubbs 账户激活";
    String EMAIL_ACTIVATE_SEND_EMAIL_FAIL = "服务器发送邮件失败！";
    String EMAIL_NO_REGISTER_NO_SEND_EMAIL = "该邮箱未注册，无法发送邮件";
}
