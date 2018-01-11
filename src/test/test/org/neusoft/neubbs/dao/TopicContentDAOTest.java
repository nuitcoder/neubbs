package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * 测试 ITopicContentDAO 接口
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicContentDAOTest {

    @Autowired
    private ITopicDAO topicDAO;

    @Autowired
    private ITopicContentDAO topicContentDAO;


    /**
     * 保存话题内容至数据库
     *      - need exist
     *           - fu_id from forum_user
     *           - ftcg_id from forum_topic_category
     *           - ft_id from forum_topic
     *
     * @return TopicContent 数据库保存后，重新查询的话题内容
     */
    private TopicContentDO savaTestTopicContentDOToDatabase() {
        //build TopicDO, sava database
        TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategoryid(1);
            topic.setTitle("topic title");
        Assert.assertEquals(1, topicDAO.saveTopic(topic));

        //build TopicContentDO, sava database
        TopicContentDO topicContent = new TopicContentDO();
            topicContent.setTopicid(topic.getId());
            topicContent.setContent("topic content");
        Assert.assertEquals(1, topicContentDAO.saveTopicContent(topicContent));

        return topicContentDAO.getTopicContentByTopicId(topic.getId());
    }


    @BeforeClass
    public static void init() {
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存话题
     */
    @Test
    @Transactional
    public void testSaveTopiContent() {
        TopicContentDO topicContent = this.savaTestTopicContentDOToDatabase();
        System.out.println("insert topic content information: "
                + topicContentDAO.getTopicContentByTopicId(topicContent.getTopicid()));
    }

    /**
     * 测试删除话题内容
     */
    @Test
    @Transactional
    public void testRemoveTopicContentByTopicId() {
        TopicContentDO topicContent = this.savaTestTopicContentDOToDatabase();
        int topicId = topicContent.getTopicid();
        Assert.assertEquals(1, topicContentDAO.removeTopicContentByTopicId(topicId));
        Assert.assertNull(topicContentDAO.getTopicContentByTopicId(topicId));

        System.out.println("delete topicid=" + topicContent.getTopicid() + " topic content");
    }

    /**
     * 测试（topicid） 获取话题内容
     */
    @Test
    @Transactional
    public void testTopicContentByTopicId() {
       TopicContentDO topicContent = this.savaTestTopicContentDOToDatabase();

       TopicContentDO selectTopicContent = topicContentDAO.getTopicContentByTopicId(topicContent.getTopicid());
       Assert.assertNotNull(selectTopicContent);

       System.out.println("get topic content information：" + selectTopicContent);
    }

    /**
     * 测试更新话题内容
     */
    @Test
    @Transactional
    public void testUpdateContentById() {
        TopicContentDO topicContent = this.savaTestTopicContentDOToDatabase();

        int topicId = topicContent.getTopicid();
        String newContent = "new topic content";
        Assert.assertEquals(1, topicContentDAO.updateContentByTopicId(topicId, newContent));
        Assert.assertEquals(newContent, topicContentDAO.getTopicContentByTopicId(topicId).getContent());

        System.out.println("update topicId=" + topicId + " topic content=<" + newContent + "> success!");
    }

    /**
     * 测试更新阅读数量（自动 +1）
     */
    @Test
    @Transactional
    public void testUpdateReadById() {
        TopicContentDO topicContent = this.savaTestTopicContentDOToDatabase();

        int topicId = topicContent.getTopicid();
        int beforeRead = topicContent.getRead();
        Assert.assertEquals(1, topicContentDAO.updateReadAddOneByTopicId(topicId));

        int afterRead = topicContentDAO.getTopicContentByTopicId(topicId).getRead();
        Assert.assertEquals(beforeRead + 1, afterRead);

        System.out.println("update topicId=" + topicId + " topic content read+1 success!");
    }

    /**
     * 测试更新喜欢人数（自动 +1）
     */
    @Test
    @Transactional
    public void testUpdateAgreeAddOneByTopicId() {
        TopicContentDO topicContent = this.savaTestTopicContentDOToDatabase();

        int topicId = topicContent.getTopicid();
        int beforeLike = topicContent.getLike();
        Assert.assertEquals(1, topicContentDAO.updateLikeAddOneByTopicId(topicId));

        int affterLike = topicContentDAO.getTopicContentByTopicId(topicId).getLike();
        Assert.assertEquals(beforeLike + 1, affterLike);

        System.out.println("update topicId=" + topicId + " topic content like+1 success!");
    }

    /**
     * 测试更新喜欢人数（自动 -1）
     */
    @Test
    @Transactional
    public void test56_updateAgreeCutOneByTopicId() {
        TopicContentDO topicContent = this.savaTestTopicContentDOToDatabase();

        int topicId = topicContent.getTopicid();
        int beforeLike = topicContent.getLike();
        Assert.assertEquals(1, topicContentDAO.updateLikeCutOneByTopicId(topicId));

        int afterLike = topicContentDAO.getTopicContentByTopicId(topicId).getLike();
        Assert.assertEquals(beforeLike - 1, afterLike);

        System.out.println("update topicId=" + topicId + " topic content like-1 success!");
    }

}
