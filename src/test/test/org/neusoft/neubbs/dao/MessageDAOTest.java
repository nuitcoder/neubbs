package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IMessageDAO;
import org.neusoft.neubbs.entity.MessageDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * 测试 IMessageDAO 接口
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class MessageDAOTest {

    @Autowired
    private IMessageDAO messageDAO;

    /**
     * 保存测试消息至数据库
     *
     * @return MessageDO 重查询的消息对象
     */
    private MessageDO saveTestMessageDOToDatabase() {
        MessageDO message = new MessageDO();
            message.setSource("System Notice");
            message.setSenderId(1);
            message.setContent("welcome all user!");
            message.setReceiverId(6);

        Assert.assertEquals(1, messageDAO.saveMessage(message));

        return messageDAO.getMessage(message.getId());
    }


    @BeforeClass
    public static void init() {
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存消息
     */
    @Test
    @Transactional
    public void testSaveMessage() {
        MessageDO message = this.saveTestMessageDOToDatabase();
        System.out.println("success save message: " + message);
    }

    /**
     * 测试获取消息信息
     */
    @Test
    @Transactional
    public void testGetMessage() {
        MessageDO message = this.saveTestMessageDOToDatabase();
        System.out.println("get message information: " + message);
    }

    /**
     * 测试更新接收时间
     */
    @Test
    @Transactional
    public void testUpdateMessageReceiveTime() {
        MessageDO message = this.saveTestMessageDOToDatabase();

        int messageId = message.getId();
        Assert.assertEquals(1, messageDAO.updateMessageReceiveTime(messageId));

        MessageDO afterMessage = messageDAO.getMessage(messageId);
        Date afterReceiveTime = afterMessage.getReceiveTime();
        Assert.assertNotNull(afterMessage);

        System.out.println("update id=" + messageId + " message receive time=<" + afterReceiveTime + ">");
    }
}
