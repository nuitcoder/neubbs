package test.org.neusoft.neubbs.db;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Transactional
public class TopicDAOTestCase {

    @Autowired
    ITopicDAO topicDAO;

    /**
     * 插入主题
     */
    @Test
    //@Transactional
    public void test1_SaveTopic(){
        TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategory("testtopic");
            topic.setTitle("测试主题");

        int effectRow = topicDAO.saveTopic(topic);
        System.out.println("插入行数：" + effectRow + "，新主题的id = " + topic.getId());
    }

    /**
     * 删除主题
     */
    @Test
    public void test2_RemoveTopicById(){
       TopicDO topic = new TopicDO();
            topic.setUserid(1);
            topic.setCategory("remove");
            topic.setTitle("即将被删除的主题");

        topicDAO.saveTopic(topic);

        int effectRow = topicDAO.removeTopicById(topic.getId());
        System.out.println("删除 id = " + topic.getId()  + " 的主题，删除行数: " + effectRow);
    }

    /**
     * 统计主题总数
     */
    @Test
    public void test3_CountTopic(){
        int topicTotal = topicDAO.countTopic();
        System.out.println("主题总数：" + topicTotal);
    }

    /**
     * 查询最新插入的id
     */
    @Ignore
    public void testGetTopicMaxId(){
        System.out.println("最新插入的 id =" + topicDAO.getTopicMaxId());
    }

    /**
     * id 查询主题
     */
    @Test
    public void test4_GetTopicById(){
        TopicDO topic = topicDAO.getTopicById(topicDAO.getTopicMaxId());
        System.out.println("查询结果：" + JsonUtil.toJSONStringByObject(topic));
    }

    /**
     *  逆序获取话题列表（指定数量）
     */
    @Test
    public void test4_ListTopicDESCByCount(){
        List<TopicDO> listTopic = topicDAO.listTopicDESCByCount(10);

        for(TopicDO topic: listTopic){
            System.out.println(JsonUtil.toJSONStringByObject(topic));
        }
    }


    /**
     * 分页查询 forum_topic 表所有记录
     */
    @Test
    public void test5_ListTopicByStartByCount(){
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
            System.out.println(JsonUtil.toJSONStringByObject(topic));
        }
    }

    /**
     * 更新分类
     */
    @Test
    public void test61_UpdateCategoryById(){
        int effectRow = topicDAO.updateCategoryById(topicDAO.getTopicMaxId(), "已经更新分类");
        System.out.println("修改标题，已更新行数：" + effectRow);
    }

    /**
     * 更新标题
     */
    @Test
    public void tes6222_UpdateTitle(){
        int effectRow = topicDAO.updateTitleById(topicDAO.getTopicMaxId(), "已经更新标题");
        System.out.println("修改标题，已更新行数：" + effectRow);
    }

    /**
     * 更新评论数（自动 +1）
     */
    @Test
    public void test63_UpdateCommentById(){
        int effectRow = topicDAO.updateCommentAddOneById(topicDAO.getTopicMaxId());
        System.out.println("评论数+1，已更新行数：" + effectRow);
    }

    /**
     * 更新评论数（自动 -1）
     */
    @Test
    public void test633_UpdateCommentById(){
        int effectRow = topicDAO.updateCommentCutOneById(topicDAO.getTopicMaxId());
        System.out.println("评论数-1，已更新行数：" + effectRow);
    }

    /**
     * 更新最后回复id
     */
    @Test
    public void test64_UpdateLastreplyuseridById(){
        int effectRow = topicDAO.updateLastreplyuseridById(topicDAO.getTopicMaxId(),20);
        System.out.println("更新最后回复人id,已更新行数：" + effectRow);
    }

    /**
     * 更新最后回复时间
     */
    @Test
    public void test65_UpdateLastreplytimeById(){
        Date date = new Date();
        int effectRow = topicDAO.updateLastreplytimeById(topicDAO.getTopicMaxId(), date);
        System.out.println("修改最后回复时间，已影响行数：" + effectRow);
    }
}
