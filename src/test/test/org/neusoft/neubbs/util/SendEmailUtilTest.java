package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.SendEmailUtil;

/**
 * SendEmailUtil 测试类
 */
@RunWith(JUnit4.class)
public class SendEmailUtilTest {
    /**
     * 测试发送激活邮件
     */
    @Test
    public void testSendEmail(){
       String email = "13202405189@163.com";
       String subject = "Neubbs 帐号激活";

       String token = SecretUtil.encodeBase64(email);

       String url  = "http://localhost:8080/neubbs?token = " + token;

       String content = "<html><head></head><body><h1>Neubbs 帐号活邮件，点击激活帐号</h1><br>"
                        + "<a href=\"" + url + "\">" + url + "</a>"
                        + "</body></html>";

        SendEmailUtil.send("Neubbs 管理员" ,email, subject, content);

        System.out.println("发送邮件成功");
    }
}
