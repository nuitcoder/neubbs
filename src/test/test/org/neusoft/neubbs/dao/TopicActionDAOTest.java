package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicActionDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.entity.TopicActionDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * 测试 ITopicAction 接口
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicActionDAOTest {

    @Autowired
    private ITopicDAO topicDAO;

    @Autowired
    private ITopicActionDAO topicActionDAO;

    /**
     * 保存测试 TopicActionDO 对象至数据库
     *      - 新建测试话题
     *      - 保存，且重新查询数据库获得对象（根据话题 id，进行查询）
     *
     * @return TopicActionDO 话题行为对象
     */
    private TopicActionDO saveTestTopicActionDOToDatabase() {
        TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategoryid(1);
            topic.setTitle("test title");

        Assert.assertEquals(1, topicDAO.saveTopic(topic));

        TopicActionDO topicAction = new TopicActionDO();
            topicAction.setTopicId(topic.getId());

        Assert.assertEquals(1, topicActionDAO.saveTopicAction(topicAction));

        return topicActionDAO.getTopicAction(topic.getId());
    }


    @BeforeClass
    public static void init() {
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存话题行为
     */
    @Test
    @Transactional
    public void testSaveTopicAction() {
        TopicActionDO topicAction = this.saveTestTopicActionDOToDatabase();
        System.out.println("success insert topic action information: " + topicAction);
    }

    /**
     * 测试获取话题行为用户 id 数组
     *      - 回复用户 id 数组
     *      - 喜欢用户 id 数组
     *      - 收藏用户 id 数组
     *      - 关注用户 id 数组
     */
    @Test
    @Transactional
    public void testGetTopicActionAllJsonArray() {
        TopicActionDO topicAction = this.saveTestTopicActionDOToDatabase();
        int topicId = topicAction.getTopicId();

        Assert.assertNotNull(topicActionDAO.getTopicActionReplyUserIdJsonArray(topicId).getReplyUserIdJsonArray());
        Assert.assertNotNull(topicActionDAO.getTopicActionLikeUserIdJsonArray(topicId).getLikeUserIdJsonArray());
        Assert.assertNotNull(topicActionDAO.getTopicActionCollectUserIdJsonArray(topicId).getCollectUserIdJsonArray());
        Assert.assertNotNull(
                topicActionDAO.getTopicActionAttentionUserIdJsonArray(topicId).getAttentionUserIdJsonArray()
        );

        System.out.println("test alone get reply, like, collect, attention topic action success!");
    }

    /**
     * 测试更新话题行为用户 id 数组（JSON 末尾插入单个用户 id）
     *      - 插入回复用户 id 数组
     *      - 插入喜欢用户 id 数组
     *      - 插入收藏用户 id 数组
     *      - 插入关注用户 id 数组
     */
    @Test
    @Transactional
    public void testUpdateTopicActionByOneUserIdToAppendEnd() {
        TopicActionDO topicAction = this.saveTestTopicActionDOToDatabase();

        int topicId = topicAction.getTopicId();
        int insertUserId = 2;
        Assert.assertEquals(1, topicActionDAO.updateReplyUserIdJsonArrayByOneUserIdToAppendEnd(topicId, insertUserId));
        Assert.assertEquals(1, topicActionDAO.updateLikeUserIdJsonArrayByOneUserIdToAppendEnd(topicId, insertUserId));
        Assert.assertEquals(1,
                topicActionDAO.updateCollectUserIdJsonArrayByOneUserIdToAppendEnd(topicId, insertUserId));
        Assert.assertEquals(1,
                topicActionDAO.updateAttentionUserIdJsonArrayByOneUserIdToAppendEnd(topicId, insertUserId));
        System.out.println("test pass update to add reply, like, collect, attention user id success!");
    }

    /**
     * 测试更新话题行为用户 id 数组（JSON 数组，根据索引, 删除指定元素）
     */
    @Test
    @Transactional
    public void testUpdateTopicActionByIndexRemoveOneUserId() {
        TopicActionDO topicAction = this.saveTestTopicActionDOToDatabase();

        int topicId = topicAction.getTopicId();
        int insertUserId = 2;
        Assert.assertEquals(1, topicActionDAO.updateReplyUserIdJsonArrayByOneUserIdToAppendEnd(topicId, insertUserId));
        Assert.assertEquals(1, topicActionDAO.updateLikeUserIdJsonArrayByOneUserIdToAppendEnd(topicId, insertUserId));
        Assert.assertEquals(1,
                topicActionDAO.updateCollectUserIdJsonArrayByOneUserIdToAppendEnd(topicId, insertUserId));
        Assert.assertEquals(1,
                topicActionDAO.updateAttentionUserIdJsonArrayByOneUserIdToAppendEnd(topicId, insertUserId));
        System.out.println("before remove: " + topicActionDAO.getTopicAction(topicId));

        int indexOfRemoveUserId = 0;
        Assert.assertEquals(1,
                topicActionDAO.updateReplyUserIdJsonArrayByIndexToRemoveOneUserId(topicId, indexOfRemoveUserId)
        );
        Assert.assertEquals(1,
                topicActionDAO.updateLikeUserIdJsonArrayByIndexToRemoveOneUserId(topicId, indexOfRemoveUserId)
        );
        Assert.assertEquals(1,
                topicActionDAO.updateCollectUserIdJsonArrayByIndexToRemoveOneUserId(topicId, indexOfRemoveUserId)
        );
        Assert.assertEquals(1,
                topicActionDAO.updateAttentionUserIdJsonArrayByIndexToRemoveOneUserId(topicId, indexOfRemoveUserId)
        );
        System.out.println("after remove: " + topicActionDAO.getTopicAction(topicId));

        System.out.println("success pass update topic action to remove reply, like, collect, attention userId!");
    }
}
