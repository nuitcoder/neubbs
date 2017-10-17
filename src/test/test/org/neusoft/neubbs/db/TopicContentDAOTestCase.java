package test.org.neusoft.neubbs.db;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * ITopicContentDAO 测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class TopicContentDAOTestCase {

    @Autowired
    ITopicContentDAO topicContentDAO;

    /**
     * 保存主题内容
     */
    @Test
    //@Transactional
    public void test1_SaveTopiContent(){
        TopicContentDO topicContent = new TopicContentDO();
            topicContent.setTopicid(2);//对应 主题
            topicContent.setContent("主题内容");

        int effectRow = topicContentDAO.saveTopicContent(topicContent);
        System.out.println("已插入行数：" + effectRow + "新主题id = " + topicContent.getId());
    }

    /**
     *删除主题内容
     */
    @Test
    public void test2_RemoveTopicContentById(){
       TopicContentDO topicContent = new TopicContentDO();
            topicContent.setTopicid(2);
            topicContent.setContent("被删除的主题内容");

        topicContentDAO.saveTopicContent(topicContent);
        int effectRow = topicContentDAO.removeTopicContentById(topicContent.getId());


        System.out.println("删除id = " + topicContent.getId() + " 的主题，已删除行数:" + effectRow);
    }

    /**
     * 查询最新插入的 id
     */
    @Ignore
    public void testGetTopicContentMaxId(){
        System.out.println("最新插入的 id = " + topicContentDAO.getTopicContentMaxId());
    }
    /**
     * 获取主题内容
     */
    @Test
    public void test3_GetTopicContentById(){
       TopicContentDO topicContent = topicContentDAO.getTopicContentById(1);

        System.out.println("查询结果：" + JsonUtils.toJSONStringByObject(topicContent));
    }

    /**
     * 更新内容
     */
    @Test
    public void test4_UpdateContentById(){
       int effectRow = topicContentDAO.updateContentById(1, "变化内容");

        System.out.println("修改主题内容，更新行数：" + effectRow);
    }

    /**
     * 更新阅读数量
     */
    @Test
    public void test5_UpdateReadById(){
        int effectRow = topicContentDAO.updateReadById(1);

        System.out.println("阅读数量+1，更新行数:" + effectRow);
    }

}
