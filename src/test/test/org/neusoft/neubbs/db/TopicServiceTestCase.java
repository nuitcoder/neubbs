package test.org.neusoft.neubbs.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试 ITopicService 接口
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicServiceTestCase {

    @Autowired
    ITopicService topicService;

    /**
     *  保存话题
     */
    @Test
    public void test_11_SaveTopic(){
        boolean result = topicService.saveTopic(1, "java", "第一个标题哟","话题内容");
        System.out.println("结果：" + result);
    }

    /**
     * 保存话题回复
     */
    @Test
    public void test_11_SaveTopicReply(){
        boolean result = topicService.saveReply(1,1,"第一条回复内容哟");
        System.out.println("结果：" + result);
    }
}
