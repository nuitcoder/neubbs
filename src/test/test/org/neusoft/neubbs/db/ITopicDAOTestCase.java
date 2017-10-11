package test.org.neusoft.neubbs.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * 测试 ITopicDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class ITopicDAOTestCase {

    @Autowired
    ITopicDAO topicDAO;

    /**
     * 保存主题
     */
    @Test
    public void testSaveTopic(){
        TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategory("Java");
            topic.setTitle("第一篇主题的标题");

        int effectRow = topicDAO.saveTopic(topic);
        System.out.println("受影响行数：" + effectRow + "--新主题的id：" + topic.getId());
    }

    /**
     * 删除主题
     */
    @Test
    public void testRemvoeTopicById(){
       TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategory("测试删除");
            topic.setTitle("即将被删除的主题");

        topicDAO.saveTopic(topic);
        int effectRow = topicDAO.removeTopicById(topic.getId());
        System.out.println("删除topic.id: " + topic.getId()  + "****已删除行数: " + effectRow);
    }

    /**
     * 查询主题
     */
    @Test
    public void testGetTopicById(){
        TopicDO topic = topicDAO.getTopicById(1);

        System.out.println("查询结果：" + JsonUtils.getJSONStringByObject(topic));
    }

    /**
     * 分页查询 forum_topic 表所有记录
     */
    @Test
    public void testListTopicByStartByCount(){
        //插入100行数据进行测试
        //for(int i = 0; i < 100; i++){
        //    TopicDO topic = new TopicDO();
        //        topic.setUserid(i);
        //        topic.setCategory("分类" + i + "种");
        //        topic.setTitle("主题" + i + "号");
        //
        //        topicDAO.saveTopic(topic);
        //        System.out.println("成功插入第" + i + "行数据");
        //}

        int startRow = 0;
        int count = 10;
        List<TopicDO> listTopic = topicDAO.listTopicByStartRowByCount(startRow, count);
        System.out.println("********从" + startRow + "行开始," + "输出" + count + "条记录***********");

        for(TopicDO topic: listTopic){
            System.out.println(JsonUtils.getJSONStringByObject(topic));
        }
    }

    /**
     * 更新分类
     */
    @Test
    public void testUpdateCategoryById(){
        int effectRow = topicDAO.updateCategoryById(1, "测试分类1");

        System.out.println("更新结果，已更新行数：" + effectRow);
    }

    /**
     * 更新标题
     */
    @Test
    public void testUpdateTitle(){
        int effectRow = topicDAO.updateTitleById(1, "标题");

        System.out.println("更新结果，已更新行数：" + effectRow);
    }

    /**
     * 更新评论数
     */
    @Test
    public void testUpdateCommentById(){
        int effectRow = topicDAO.updateCommentById(1);

        System.out.println("更新结果，已更新行数：" + effectRow);
    }

    /**
     * 更新最后回复时间
     */
    @Test
    public void testUpdateLastreplaytimeById(){
        Date date = new Date();
        int effectRow = topicDAO.updateLastreplytimeById(1, date);

        System.out.println("更新结果，已影响行数：" + effectRow);
    }
}
