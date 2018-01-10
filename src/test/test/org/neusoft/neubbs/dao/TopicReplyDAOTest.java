package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.data.DynamicSwitchDataSource;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 测试 ITopicReplyDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicReplyDAOTest {

    @Autowired
    private ITopicDAO topicDAO;

    @Autowired
    private ITopicReplyDAO topicReplyDAO;

    /**
     * 保存回复至数据库
     *      - need exist
     *          - fu_id from forum_user
     *          - ft_id from forum_topic
     *
     * @return TopicReplyDO 数据库保存后，重新查询回复
     */
    private TopicReplyDO saveTestReplyToDatabase() {
        //build TopicDO, sava database
        TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategoryid(1);
            topic.setTitle("topic title");
        Assert.assertEquals(1, topicDAO.saveTopic(topic));

        //build TopicReplyDO, save database
        TopicReplyDO reply = new TopicReplyDO();
            reply.setUserid(1);
            reply.setTopicid(topic.getId());
            reply.setContent("reply content");
        Assert.assertEquals(1, topicReplyDAO.saveTopicReply(reply));

        return topicReplyDAO.getTopicReplyById(reply.getId());
    }


    @BeforeClass
    public static void init() {
        DynamicSwitchDataSource.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存回复
     */
    @Test
    @Transactional
    public void testSaveTopicReply() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();
        System.out.println("insert reply information：" + reply);
    }

    /**
     * 测试删除回复
     */
    @Test
    @Transactional
    public void testRemoveTopicReplyById() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        int replyId = reply.getId();
        Assert.assertEquals(1, topicReplyDAO.removeTopicReplyById(replyId));
        Assert.assertNull(topicReplyDAO.getTopicReplyById(replyId));

        System.out.println("delete replyId=" + reply.getId() + " reply");
    }

    /**
     * 测试删除指定 topicId 所有回复
     */
    @Test
    @Transactional
    public void testRemoveListTopicReplyByTopicId() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        Assert.assertEquals(1, topicReplyDAO.removeTopicAllReplyByTopicId(reply.getTopicid()));
        Assert.assertNull(topicReplyDAO.getTopicReplyById(reply.getId()));

        System.out.println("delete topicid=" + reply.getTopicid() + " topic all reply");
    }

    /**
     * 测试获取话题总数
     */
    @Test
    public void testCountReply() {
        int replyTotals = topicReplyDAO.countReply();
        System.out.println("count topic reply totals=" + replyTotals);
    }

    /**
     * 测试统计用户回复数
     */
    @Test
    public void testCountReplyByUserId() {
        int userReplytTotals = topicReplyDAO.countReplyByUserId(6);
        System.out.println("count user topic reply totals=" + userReplytTotals);
    }

    /**
     * 测试获取最大的话题回复 id
     */
    @Test
    @Transactional
    public void testGetMaxTopicReplyId() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        Assert.assertEquals(reply.getId(), (Integer) topicReplyDAO.getMaxTopicReplyId());

        System.out.println("success get max reply id");
    }

    /**
     * 测试获取话题回复
     */
    @Test
    @Transactional
    public void testGetTopicReplyById() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        int replyId = reply.getId();
        System.out.println("get reply information：" + topicReplyDAO.getTopicReplyById(replyId));
    }

    /**
     * 测试获取指定 topicid 的所有回复
     */
    @Test
    @Transactional
    public void testListTopicReplyByTopicId() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        int topicId = reply.getTopicid();
        List<TopicReplyDO> listTopicReply = topicReplyDAO.listTopicReplyByTopicId(topicId);
        Assert.assertTrue(listTopicReply.size() >= 1);

        int replyCount = 1;
        for(TopicReplyDO topicReply: listTopicReply){
            System.out.println("ouput reply information (" + (replyCount++) + "): "
                    + JsonUtil.toJSONStringByObject(topicReply));
        }

        System.out.println("success get topicid=" + topicId + " all reply list");
    }

    /**
     * 测试更新回复内容
     */
    @Test
    @Transactional
    public void testUpdateContentByIdByContent() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        int replyId = reply.getId();
        String beforeContent = reply.getContent();
        String newContent = "new reply content";
        Assert.assertEquals(1, topicReplyDAO.updateContentByIdByContent(replyId, newContent));

        String afterContent = topicReplyDAO.getTopicReplyById(replyId).getContent();
        Assert.assertNotEquals(beforeContent, afterContent);

        System.out.println("update replyid=" + replyId + " reply content=<" + newContent + "> success!");
    }

    /**
     * 测试更新回复点赞数（自动 +1）
     */
    @Test
    @Transactional
    public void testUpdateAgreeAddOneById() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        int replyId = reply.getId();
        int beforeAgree = reply.getAgree();
        Assert.assertEquals(1, topicReplyDAO.updateAgreeAddOneById(replyId));

        int afterAgree = topicReplyDAO.getTopicReplyById(replyId).getAgree();
        Assert.assertEquals(beforeAgree + 1, afterAgree);

        System.out.println("update replyid=" + replyId + " reply agree+1 success!");
    }

    /**
     * 测试回复点赞数（自动 -1）
     */
    @Test
    @Transactional
    public void testUpdateAgreeCutOneById() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        int replyId = reply.getId();
        int beforeAgree = reply.getAgree();
        Assert.assertEquals(1, topicReplyDAO.updateAgreeCutOneById(replyId));

        int afterAgree = topicReplyDAO.getTopicReplyById(replyId).getAgree();
        Assert.assertEquals(beforeAgree - 1, afterAgree);

        System.out.println("update replyid=" + replyId + " reply agree-1 success!");
    }

    /**
     * 测试回复反对数（自动 +1）
     */
    @Test
    @Transactional
    public void testUpdateOpposeAddOneById() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        int replyId = reply.getId();
        int beforeOppose = reply.getOppose();
        Assert.assertEquals(1, topicReplyDAO.updateOpposeAddOneById(replyId));

        int afterOppose = topicReplyDAO.getTopicReplyById(replyId).getOppose();
        Assert.assertEquals(beforeOppose + 1, afterOppose);

        System.out.println("update replyid=" + replyId + " reply oppose+1 success!");
    }

    /**
     * 测试回复反对数（自动 -1）
     */
    @Test
    @Transactional
    public void testUpdateOpposeCutOneById() {
        TopicReplyDO reply = this.saveTestReplyToDatabase();

        int replyId = reply.getId();
        int beforeOppose = reply.getOppose();
        Assert.assertEquals(1, topicReplyDAO.updateOpposeCutOneById(replyId));

        int afterOppose = topicReplyDAO.getTopicReplyById(replyId).getOppose();
        Assert.assertEquals(beforeOppose - 1, afterOppose);

        System.out.println("update replyid=" + replyId + " reply oppose-1 success!");
    }
}
