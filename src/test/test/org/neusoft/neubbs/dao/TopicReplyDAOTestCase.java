package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试 ITopicReplyDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicReplyDAOTestCase {

    @Autowired
    ITopicReplyDAO topicReplyDAO;

    @BeforeClass
    public static void init() {
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 保存回复
     *      - 用户 id 受外键约束
     *      - 话题 id 受外键约束
     */
    @Ignore
    public void testSaveTopicReply(){
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(1);
            topicReply.setTopicid(1);
            topicReply.setContent("第一篇评论内容");

        Assert.assertNotEquals(topicReplyDAO.saveTopicReply(topicReply), 0);
        System.out.println("插入话题回复：" + JsonUtil.toJSONStringByObject(topicReply));
    }

    /**
     * 保存 100 条回复
     */
    @Ignore
    public void testSaveTopicReply_100() {
        TopicReplyDO topicReply = new TopicReplyDO();
        for(int i = 1; i <= 100; i++) {
            topicReply.setUserid(i % 5 + 1);
            topicReply.setTopicid(1 + (int)(Math.random() * 100));
            topicReply.setContent("第 " + i + " 条回复内容");

            Assert.assertNotEquals(topicReplyDAO.saveTopicReply(topicReply), 0);
            System.out.println("成功插入 " + i + " 条回复！");
        }
    }

    /**
     * id 删除回复
     */
    @Ignore
    public void testRemoveTopicReplyById(){
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(1);
            topicReply.setTopicid(1);
            topicReply.setContent("即将删除的回复");

        Assert.assertNotEquals(topicReplyDAO.saveTopicReply(topicReply), 0);
        Assert.assertNotEquals(topicReplyDAO.removeTopicReplyById(topicReply.getId()), 0);
        System.out.println("删除话题回复 replyId = " + topicReply.getId());
    }

    /**
     * 删除回复（指定话题id，所有回复）
     */
    @Ignore
    public void testRemoveListTopicReplyByTopicId(){
        int effectRow = topicReplyDAO.removeListTopicReplyByTopicId(2);
        Assert.assertNotEquals(effectRow, 0);
        System.out.println("删除指定话题 id 的 " + effectRow + " 条回复！");
    }

    /**
     * 获取最大回复 id（ftr_id，最新插入id）
     */
    @Test
    public void testGetTopicReplyMaxId(){
        System.out.println("最大回复 id = " + topicReplyDAO.getTopicReplyMaxId());
    }

    /**
     * replyId 获取指定回复
     */
    @Test
    public void testGetTopicReplyById(){
        TopicReplyDO topicReply = topicReplyDAO.getTopicReplyById(topicReplyDAO.getTopicReplyMaxId());
        Assert.assertNotNull(topicReply);
        System.out.println("查询回复信息：" + JsonUtil.toJSONStringByObject(topicReply));
    }

    /**
     * 指定话题 id，获取话题回复列表
     */
    @Test
    public void testListTopicReplyByTopicId(){
        List<TopicReplyDO> listTopicReply = topicReplyDAO.listTopicReplyByTopicId(1);

        System.out.println("*************************** 查询 topicid = 1 的话题回复 ****************************");
        for(TopicReplyDO topicReply: listTopicReply){
            System.out.println(JsonUtil.toJSONStringByObject(topicReply));
        }
    }

    /**
     * replyId 更新回复内容
     */
    @Ignore
    public void testUpdateContentByIdByContent(){
        String newContent = "新回复内容";
        Assert.assertNotEquals(topicReplyDAO.updateContentByIdByContent(topicReplyDAO.getTopicReplyMaxId(), newContent), 0);
    }

    /**
     * 回复点赞数（自动 +1）
     */
    @Ignore
    public void testUpdateAgreeAddOneById(){
        Assert.assertNotEquals(topicReplyDAO.updateAgreeAddOneById(topicReplyDAO.getTopicReplyMaxId()), 0);
    }

    /**
     * 回复点赞数（自动 -1）
     */
    @Ignore
    public void testUpdateAgreeCutOneById(){
        Assert.assertNotEquals(topicReplyDAO.updateAgreeCutOneById(topicReplyDAO.getTopicReplyMaxId()), 0);
    }

    /**
     * 回复反对数（自动 +1）
     */
    @Ignore
    public void testUpdateOpposeAddOneById(){
        Assert.assertNotEquals(topicReplyDAO.updateOpposeAddOneById(topicReplyDAO.getTopicReplyMaxId()), 0);
    }

    /**
     * 回复反对数（自动 -1）
     */
    @Ignore
    public void testUpdateOpposeCutOneById(){
        Assert.assertNotEquals(topicReplyDAO.updateOpposeCutOneById(topicReplyDAO.getTopicReplyMaxId()), 0);
    }
}
