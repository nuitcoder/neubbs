package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * 测试 ITopicDAO 接口
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicDAOTest {

    @Autowired
    private ITopicDAO topicDAO;

    /**
     * 获取测试对象
     *      - need ft_id=1 from forum_user
     *      - need ftcg_id=1 from forum_topic_category
     *
     * @return TopicDO 测试对象
     */
    private TopicDO getTestTopicDO() {
        TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategoryid(1);
            topic.setTitle("new topic content");

        return topic;
    }

    /**
     * 保存测试话题对象至数据库
     *
     * @return TopicDO 数据库保存后，重新 id 查询的测试对象
     */
    private TopicDO saveTestTopicToDB() {
        TopicDO topic = this.getTestTopicDO();
        Assert.assertEquals(1, topicDAO.saveTopic(topic));

        return topicDAO.getTopicById(topic.getId());
    }

    @BeforeClass
    public static void init() {
        //set local database source
       SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存话题
     */
    @Test
    @Transactional
    public void testSaveTopic() {
        TopicDO topic = this.getTestTopicDO();
        Assert.assertEquals(1, topicDAO.saveTopic(topic));
        Assert.assertTrue(topic.getId() > 0);
        Assert.assertNotNull(topicDAO.getTopicById(topic.getId()));

        System.out.println("insert topic informatioin : "
                + JsonUtil.toJSONStringByObject(topicDAO.getTopicById(topic.getId())));
    }

    /**
     * 测试删除话题
     */
    @Test
    @Transactional
    public void testRemoveTopicById(){
        TopicDO topic = this.saveTestTopicToDB();
        Assert.assertEquals(1, topicDAO.removeTopicById(topic.getId()));

        System.out.println("remove id=" + topic.getId() + " topic");
    }

    /**
     * 测试统计话题总数
     */
    @Test
    @Transactional
    public void testCountTopic() {
        this.saveTestTopicToDB();

        int topicTotal = topicDAO.countTopic();
        Assert.assertTrue(topicTotal > 0);

        System.out.println("total topic count：" + topicTotal);
    }

    /**
     * 测试（分类）统计话题总数
     */
    @Test
    @Transactional
    public void testCountTopicByCategory() {
        this.saveTestTopicToDB();

        int categoryId = 1;
        int topicTotal = topicDAO.countTopicByCategoryId(categoryId);
        Assert.assertTrue(topicTotal >= 1);

        System.out.println("categoryid=" + categoryId + " topic total count: " + topicTotal);
    }

    /**
     * 测试（用户 id）统计话题总数
     */
    @Test
    @Transactional
    public void testCountTopicByUserId() {
        this.saveTestTopicToDB();

        int userId = 1;
        int topicTotal = topicDAO.countTopicByUserId(userId);
        Assert.assertTrue(topicTotal >= 1);

        System.out.println("userid=" + userId + " topic total count: " +  topicTotal);
    }

    /**
     * 测试（话题分类id，用户 id）统计话题总数
     */
    @Test
    @Transactional
    public void testCountTopicByCategoryIdByUserId() {
        this.saveTestTopicToDB();

        int categoryId = 1, userId = 1;
        int topicTotal = topicDAO.countTopicByCategoryIdByUserId(categoryId, userId);
        Assert.assertTrue(topicTotal >= 1);

        System.out.println("categoryid=" + categoryId + "and userid=" + userId + " topic total count: " + topicTotal);
    }

    /**
     * 测试获取最大的话题 id
     *      - 最新发布的话题
     */
    @Test
    @Transactional
    public void testMaxTopicId() {
        this.saveTestTopicToDB();

        int maxTopicId = topicDAO.getMaxTopicId();
        Assert.assertTrue(maxTopicId > 0);

        System.out.println("get max topic id=" + maxTopicId);
    }

    /**
     * 测试 （id）查询话题
     */
    @Test
    @Transactional
    public void testTopicById() {
        TopicDO topic = this.saveTestTopicToDB();
        TopicDO selectTopic = topicDAO.getTopicById(topic.getId());
        Assert.assertNotNull(selectTopic);

        System.out.println("get id=" + topic.getId() + " topic information: "
                + JsonUtil.toJSONStringByObject(selectTopic));
    }

    /**
     * 测试（降序，仅输入 count）获取话题列表
     */
    @Test
    @Transactional
    public void testListTopicOrderByCreatetimeDESCByRepliesDESCLimitTen() {
        this.saveTestTopicToDB();

        List<TopicDO> listTopic = topicDAO.listTopicOrderByCreatetimeDESCByRepliesDESCLimitTen();
        Assert.assertTrue(listTopic.size() >= 1);

        for(TopicDO topic: listTopic){
            System.out.println("output topic information：" + JsonUtil.toJSONStringByObject(topic));
        }
    }


    /**
     * 测试（降序）获取话题列表
     */
    @Test
    @Transactional
    public void testListTopicByStartByCount() {
        this.saveTestTopicToDB();

        //first page
        int startRow = 0, count = 10;
        List<TopicDO> listTopic = topicDAO.listTopicDESCByStartRowByCount(startRow, count);
        Assert.assertTrue(listTopic.size() >= 1);

        //foreach list
        int recordCount = 1;
        for(TopicDO topic: listTopic){
            System.out.println("output topic information (No." + (recordCount++) + " recoders): "
                    + JsonUtil.toJSONStringByObject(topic));
        }
    }


    /**
     * 测试（降序，分类）获取话题列表
     */
    @Test
    @Transactional
    public void testListTopicByStartRowByCountByCategory() {
        this.saveTestTopicToDB();

        //first page
        int startRow = 0, count = 10;
        int categoryId = 1;
        List<TopicDO> listTopic = topicDAO.listTopicDESCByStartRowByCountByCategoryId(startRow, count, categoryId);
        Assert.assertTrue(listTopic.size() >= 1);

        //foreach list
        int recordCount = 1;
        for(TopicDO topic: listTopic){
            System.out.println("output categoryid=" + categoryId
                    + " topic information (No." + (recordCount++) + " recoders): "
                    + JsonUtil.toJSONStringByObject(topic));
        }
    }

    /**
     * 测试（降序，用户 id 分类）获取话题列表
     */
    @Test
    @Transactional
    public void testListTopicByStartRowByCountByUsername() {
        this.saveTestTopicToDB();

        //first page
        int startRow = 0, count = 10;
        int userId = 1;
        List<TopicDO> listTopic = topicDAO.listTopicDESCByStartRowByCountByUserId(startRow, count, userId);
        Assert.assertTrue(listTopic.size() >= 1);

        //foreach list
        int recordCount = 1;
        for(TopicDO topic: listTopic){
            System.out.println("output userid=" + userId
                    + " topic information (No." + (recordCount++) + " recoders): "
                    + JsonUtil.toJSONStringByObject(topic));
        }
    }


    /**
     * 测试（降序，话题分类 id，用户 id）获取话题列表
     */
    @Test
    @Transactional
    public void testListTopicDESCByStartRowByCountByCategoryIdByUserId() {
        this.saveTestTopicToDB();

        //first page
        int startRow = 0, count = 10;
        int ccategoryId = 1, userId = 1;
        List<TopicDO> listTopic
                = topicDAO.listTopicDESCByStartRowByCountByCategoryIdByUserId(startRow, count, ccategoryId,userId);
        Assert.assertTrue(listTopic.size() >= 1);

        //foreach list
        int recordCount = 1;
        for(TopicDO topic: listTopic){
            System.out.println("output categoryid=" + ccategoryId +" and userid=" + userId
                    + " topic information (No." + (recordCount++) + " recoders): "
                    + JsonUtil.toJSONStringByObject(topic));
        }
    }

    /**
     * 测试更新话题分类
     *      - need insert categoryId == 2;
     */
    @Test
    @Transactional
    public void testUpdateCategoryById() {
        TopicDO topic = this.saveTestTopicToDB();

        int newCategoryId = 2;
        Assert.assertEquals(1, topicDAO.updateCategoryById(topic.getId(), newCategoryId));
        Assert.assertNotEquals(topic.getCategoryid(), topicDAO.getTopicById(topic.getId()).getCategoryid());

        System.out.println("update topicId=" + topic.getId() + " topic categoryid=<" + newCategoryId + "> success!");
    }

    /**
     * 测试更新话题名
     */
    @Test
    @Transactional
    public void testUpdateTitleById() {
        TopicDO topic = this.saveTestTopicToDB();

        String newTitle = "test update new Title";
        Assert.assertEquals(1, topicDAO.updateTitleById(topic.getId(), newTitle));
        Assert.assertNotEquals(topic.getTitle(), topicDAO.getTopicById(topic.getId()).getTitle());

        System.out.println("update topicId=" + topic.getId() + " topic title=<" + newTitle+ "> success!");
    }

    /**
     * 测试更新评论回复数（自动 +1）
     */
    @Test
    @Transactional
    public void testUpdateRepliesAddOneById() {
        TopicDO topic = this.saveTestTopicToDB();

        Assert.assertEquals(1, topicDAO.updateRepliesAddOneById(topic.getId()));

        //default 0 + 1 = 1
        Integer expectReplies = 1;
        Assert.assertEquals(expectReplies, topicDAO.getTopicById(topic.getId()).getReplies());

        System.out.println("update topicId=" + topic.getId() + " topic replies+1 success!");
    }

    /**
     * 测试更新评论回复数（自动 -1）
     */
    @Test
    @Transactional
    public void testUpdateRepliesCutOneById() {
        TopicDO topic = this.saveTestTopicToDB();

        Assert.assertEquals(1, topicDAO.updateRepliesCutOneById(topic.getId()));

        //default 0 - 1 = -1
        Integer expectReplies = -1;
        Assert.assertEquals(expectReplies, topicDAO.getTopicById(topic.getId()).getReplies());

        System.out.println("update topicId=" + topic.getId() + " topic replies-1 success!");
    }

    /**
     * 测试更新最后回复人 id
     */
    @Test
    @Transactional
    public void testUpdateLastReplyUserIdById() {
        TopicDO topic = this.saveTestTopicToDB();

        Integer newLastReplyUserId = 2;
        Assert.assertEquals(1, topicDAO.updateLastReplyUserIdById(topic.getId(), newLastReplyUserId));
        Assert.assertNotEquals(topic.getLastreplyuserid(),
                topicDAO.getTopicById(topic.getId()).getLastreplyuserid());

        System.out.println("update topicId=" + topic.getId()
                + " topic lastreplyuserid=<" + newLastReplyUserId+ "> success!");
    }

    /**
     * 测试更新最后回复时间
     */
    @Test
    @Transactional
    public void testUpdateLastReplyTimeById() {
        TopicDO topic = this.saveTestTopicToDB();

        Date newLastReplyTime = new Date();
        Assert.assertEquals(1, topicDAO.updateLastReplyTimeById(topic.getId(), newLastReplyTime));
        Assert.assertNotEquals(topic.getLastreplytime(),
                topicDAO.getTopicById(topic.getId()).getLastreplytime());

        System.out.println("update topicId=" + topic.getId()
                + " topic lastreplytime=<" + newLastReplyTime+ "> success!");
    }
}
