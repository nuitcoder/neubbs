package test.org.neusoft.neubbs.db;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Transactional
public class TopicReplyDAOTestCase {

    @Autowired
    ITopicReplyDAO topicReplyDAO;

    /**
     * 保存回复
     */
    @Test
    //@Transactional
    public void test1_SaveTopicReply(){
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(1);
            topicReply.setTopicid(1);
            topicReply.setContent("第一篇评论内容");

        int effectRow = topicReplyDAO.saveTopicReply(topicReply);
        System.out.println("插入行数：" + effectRow + "，新回复 id = " + topicReply.getId());
    }

    /**
     * 删除回复
     */
    @Test
    public void test2_RemoveTopicReplyById(){
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setUserid(2);
            topicReply.setTopicid(2);
            topicReply.setContent("即将删除的回复");

        topicReplyDAO.saveTopicReply(topicReply);

        int effectRow = topicReplyDAO.removeTopicReplyById(topicReply.getId());
        System.out.println("删除id = " + topicReply.getId() + " 的主题，删除行数 " + effectRow);
    }

    /**
     * 删除回复（指定话题id，所有回复）
     */
    @Test
    public void test22_RemoveTopicReplyByTopicId(){
        TopicReplyDO topicReply = new TopicReplyDO();
        for(int i = 0; i < 5; i++){
            topicReply.setUserid(3);
            topicReply.setTopicid(3);
            topicReply.setContent("即将被删除话题id=3的5条回复之一");

            topicReplyDAO.saveTopicReply(topicReply);
        }
        System.out.println("成功插入 5 条 topicId = 3 的回复");

        int effectRow = topicReplyDAO.removeTopicReplyByTopicId(3);
        System.out.println("删除 话题id = 3 的所有回复，删除行数：" + effectRow);

    }

    /**
     * 获取最近插入的 id
     */
    @Ignore
    public void testGetTopicReplyMaxId(){
        System.out.println("最近插入的id:" + topicReplyDAO.getTopicReplyMaxId());
    }

    /**
     * id 获取指定回复
     */
    @Test
    public void test3_GetTopicReplyById(){
        int maxId = topicReplyDAO.getTopicReplyMaxId();
        System.out.println("最新插入的id ：" + maxId);

        TopicReplyDO topicReply = topicReplyDAO.getTopicReplyById(maxId);

        System.out.println("查询 id = 1 的回复，查询结果：" + JsonUtil.toJSONStringByObject(topicReply));
    }

    /**
     * 获取话题回复列表（指定话题id）
     */
    @Test
    public void test33_listTopicReplyByTopicId(){
        List<TopicReplyDO> listTopicReply = topicReplyDAO.listTopicReplyByTopicId(1);

        for(TopicReplyDO topicReply: listTopicReply){
            System.out.println(JsonUtil.toJSONStringByObject(topicReply));
        }
    }

    /**
     * 更新回复内容
     */
    @Test
    public void test4_UpdateContentByIdByContent(){
        int effectRow = topicReplyDAO.updateContentByIdByContent(1, "已更新回复内容");
        System.out.println("更新 id = 1 的回复,\"内容\"，更新行数：" + effectRow);
    }

    /**
     * 点赞数 + 1
     */
    @Test
    public void test5_UpdateAgreeAddOneById(){
        int effectRow = topicReplyDAO.updateAgreeAddOneById(topicReplyDAO.getTopicReplyMaxId());
        System.out.println("更新回复，点赞数+1，更新结果" + effectRow);
    }

    /**
     * 点赞数 - 1
     */
    @Test
    public void test55_UpdateAgreeCutOneById(){
        int effectRow = topicReplyDAO.updateAgreeCutOneById(topicReplyDAO.getTopicReplyMaxId());
        System.out.println("更新回复，点赞数-1，更新结果" + effectRow);
    }

    /**
     * 反对数 + 1
     */
    @Test
    public void test6_UpdateOpposeAddOneById(){
        int effectRow = topicReplyDAO.updateOpposeAddOneById(topicReplyDAO.getTopicReplyMaxId());
        System.out.println("更新回复，反对数+1，更新行数：" + effectRow);
    }


    /**
     * 反对数 - 1
     */
    @Test
    public void test66_UpdateOpposeCutOneById(){
        int effectRow = topicReplyDAO.updateOpposeCutOneById(topicReplyDAO.getTopicReplyMaxId());
        System.out.println("更新回复，反对数-1，更新行数：" + effectRow);
    }}
