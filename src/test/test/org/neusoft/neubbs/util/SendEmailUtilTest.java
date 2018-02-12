package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.SendEmailUtil;
import org.neusoft.neubbs.utils.StringUtil;

/**
 * SendEmailUtil 测试类
 *      - 测试 send()
 */
@RunWith(JUnit4.class)
public class SendEmailUtilTest {

    /**
     * 测试 send()
     */
    @Test
    public void testSendEmail(){
        String sendNickName = "Neubbs";
        String receiveEmail = "liushuwei0925@gmail.com";
        String sendSubject = "Neubbs 账户激活";

        //splice mail content（activateUrl + token）
        String token = SecretUtil.encodeBase64(receiveEmail + "-" + StringUtil.getTodayTwentyFourClockTimestamp());
        String activateUrl = "http://localhost:8080/account/validate?token=";
        String sendEmailContent = StringUtil.generateActivateMailHtmlContent(activateUrl + token);

        //need to go 'receiveEmail' to validate
        SendEmailUtil.send(sendNickName, receiveEmail, sendSubject, sendEmailContent);
        System.out.println("already send to " + receiveEmail);
    }
}
