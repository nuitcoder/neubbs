package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ITopicContentDAO 测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicContentDAOTest {

    @Autowired
    ITopicContentDAO topicContentDAO;

    @BeforeClass
    public static void init() {
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 插入话题内容
     *      - 话题 id，外键约束
     */
    @Ignore
    public void testSaveTopiContent(){
        TopicContentDO topicContent = new TopicContentDO();
            topicContent.setTopicid(1);
            topicContent.setContent("主题内容");

        Assert.assertNotEquals(topicContentDAO.saveTopicContent(topicContent), 0);
        System.out.println("保存话题内容：" + JsonUtil.toJSONStringByObject(topicContent));
    }

    /**
     * topicid 删除话题内容
     */
    @Ignore
    public void test2_RemoveTopicContentById(){
       TopicContentDO topicContent = new TopicContentDO();
            topicContent.setTopicid(2);
            topicContent.setContent("被删除的主题内容");

        Assert.assertNotEquals(topicContentDAO.saveTopicContent(topicContent), 0);
        Assert.assertNotEquals(topicContentDAO.removeTopicContentById(topicContent.getId()), 0);
        System.out.println("删除话题 id = " + topicContent.getId());
    }

    /**
     * 查询话题内容 id 最大值(最新插入 id)
     */
    @Test
    public void testTopicContentMaxId(){
        System.out.println("最新话题内容（ftc_id） id = " + topicContentDAO.getTopicContentMaxId());
    }
    /**
     * topicid 获取主题内容
     */
    @Test
    public void testTopicContentById(){
       TopicContentDO topicContent = topicContentDAO.getTopicContentById(1);
       Assert.assertNotNull(topicContent);
       System.out.println("话题内容信息：" + JsonUtil.toJSONStringByObject(topicContent));
    }

    /**
     * 更新内容
     */
    @Ignore
    public void testUpdateContentById(){
       String newContent = "新的话题内容";
       Assert.assertNotEquals(topicContentDAO.updateContentByTopicId(1, newContent), 0);
    }

    /**
     * 更新阅读数量（自动 +1）
     */
    @Ignore
    public void testUpdateReadById(){
        Assert.assertNotEquals(topicContentDAO.updateReadAddOneByTopicId(1), 0);
    }

    /**
     * 更新赞同数（自动 +1）
     */
    @Ignore
    public void testUpdateAgreeAddOneByTopicId(){
        Assert.assertNotEquals(topicContentDAO.updateAgreeAddOneByTopicId(1), 0);
    }

    /**
     * 更新赞同数（自动 -1）
     */
    @Ignore
    public void test56_updateAgreeCutOneByTopicId(){
        Assert.assertNotEquals(topicContentDAO.updateAgreeCutOneByTopicId(1), 0);
    }

}
