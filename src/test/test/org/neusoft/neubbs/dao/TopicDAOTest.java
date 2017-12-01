package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * 测试 ITopicDAO 接口
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicDAOTest {

    @Autowired
    ITopicDAO topicDAO;

   @BeforeClass
   public static void init() {
       SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
   }

    /**
     * 插入话题
     */
    @Ignore
    public void testSaveTopic(){
        TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategory("testtopic");
            topic.setTitle("测试话题");

        Assert.assertNotEquals(topicDAO.saveTopic(topic), 0);
        System.out.println("插入新话题:" + JsonUtil.toJSONStringByObject(topic));
    }

    /**
     * 插入 100 条话题，用于测试
     */
    @Ignore
    public void testSaveTopic_100() {
        //插入100行数据进行测试（用户 id 必须存在，受外键约束）
        for(int i = 1; i <= 100; i++){
            TopicDO topic = new TopicDO();
            topic.setUserid(i % 5 + 1);
            topic.setCategory("分类" + i + "种");
            topic.setTitle("主题" + i + "号");

            topicDAO.saveTopic(topic);
            System.out.println("成功插入第" + i + "行数据");
        }
    }

    /**
     * 删除主题
     */
    @Test
    public void testRemoveTopicById(){
       TopicDO topic = new TopicDO();
            topic.setUserid(98);
            topic.setCategory("remove");
            topic.setTitle("即将被删除的话题");

        Assert.assertNotEquals(topicDAO.saveTopic(topic), 0);
        Assert.assertNotEquals(topicDAO.removeTopicById(topic.getId()), 0);
        System.out.println("删除话题 id = " + topic.getId());
    }

    /**
     * 统计话题总数
     */
    @Test
    public void testCountTopic(){
        System.out.println("话题总数：" + topicDAO.countTopic());
    }

    /**
     * 查询 id 最大值（最新插入 id）
     */
    @Test
    public void testTopicMaxId(){
        System.out.println("最大值 id = " + topicDAO.getTopicMaxId());
    }

    /**
     * id 查询话题
     */
    @Test
    public void testTopicById(){
        TopicDO topic = topicDAO.getTopicById(topicDAO.getTopicMaxId());

        Assert.assertNotNull(topic);
        System.out.println("id 查询话题（最新插入）" + JsonUtil.toJSONStringByObject(topic));
    }

    /**
     * 获取话题列表（逆序-最新插入，指定数量）
     */
    @Test
    public void testListTopicDESCByCount(){
        List<TopicDO> listTopic = topicDAO.listTopicDESCByCount(10);

        for(TopicDO topic: listTopic){
            System.out.println("话题信息：" + JsonUtil.toJSONStringByObject(topic));
        }
    }


    /**
     * 获取话题列表（指定开始行数，数量）
     */
    @Test
    public void testListTopicByStartByCount(){
        int startRow = 0;
        int count = 10;
        List<TopicDO> listTopic = topicDAO.listTopicByStartRowByCount(startRow, count);

        System.out.println("********从" + startRow + "行开始," + "输出" + count + "条记录***********");
        for(TopicDO topic: listTopic){
            System.out.println(JsonUtil.toJSONStringByObject(topic));
        }
        System.out.println("*************************** 结束 ****************************");
    }


    /**
     * 获取话题列表（分类获取）
     */
    @Test
    public void testListTopicByStartRowByCountByCategory() {
        int startRow = 0;
        int count = 10;
        String category = "分类 1";

        List<TopicDO> listTopic = topicDAO.listTopicByStartRowByCountByCategory(startRow, count, category);
        for (TopicDO topic: listTopic) {
            System.out.println(JsonUtil.toJSONStringByObject(topic));
        }
    }

    /**
     * 更新分类
     */
    @Ignore
    public void testUpdateCategoryById(){
        String newCategory = "新更新分类";
        Assert.assertNotEquals(topicDAO.updateCategoryById(topicDAO.getTopicMaxId(), newCategory), 0);
    }

    /**
     * 更新标题
     */
    @Ignore
    public void testUpdateTitle(){
        String newTitle = "新标题";
        Assert.assertNotEquals(topicDAO.updateTitleById(topicDAO.getTopicMaxId(), newTitle), 0);
    }

    /**
     * 更新评论数（自动 +1）
     */
    @Ignore
    public void testUpdateCommentAddOneById(){
        Assert.assertNotEquals(topicDAO.updateCommentAddOneById(topicDAO.getTopicMaxId()), 0);
    }

    /**
     * 更新评论数（自动 -1）
     */
    @Ignore
    public void testUpdateCommentCutOneById(){
        Assert.assertNotEquals(topicDAO.updateCommentCutOneById(topicDAO.getTopicMaxId()), 0);
    }

    /**
     * 更新最后回复 id
     *      - id 有外键约束
     */
    @Ignore
    public void testUpdateLastreplyuseridById(){
        int newLastReplyUserId = 1;
        Assert.assertNotEquals(topicDAO.updateLastreplyuseridById(topicDAO.getTopicMaxId(), newLastReplyUserId), 0);
    }

    /**
     * 更新最后回复时间
     */
    @Ignore
    public void testUpdateLastreplytimeById(){
        Assert.assertNotEquals(topicDAO.updateLastreplytimeById(topicDAO.getTopicMaxId(), new Date()), 0);
    }
}
