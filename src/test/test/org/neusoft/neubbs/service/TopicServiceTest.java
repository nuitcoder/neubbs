package test.org.neusoft.neubbs.service;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * 测试 ITopicService 接口
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicServiceTest {

    @Autowired
    private ITopicService topicService;

    @BeforeClass
    public static void init() {
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 保存 100 条话题与 1000条回复
     */
    @Ignore
    public void testSaveTopicAndReply() throws Exception{
        //插入 100 条话题（用户 id[1 ~ 6]）
        for (int i = 1; i <= 100; i++) {
            int userId= 1 + (int)(Math.random() * 6);
            String category = "分类 " + i;
            String title = "标题 " + i;
            String topicContent = "内容 " + i;

            int topicId = topicService.saveTopic(userId, category, title, topicContent);
            System.out.println("success insert topicId = " + topicId + " topic!");
        }

        //插入 1000 条回复（用户 id[1 ~ 6]，话题 id[1 ~ 100]）
        for(int i = 1; i <= 1000; i++) {
            int userId = 1 + (int)(Math.random() * 6);
            int topicId = 1 + (int)(Math.random() * 100);
            String replyContent = "回复" + i;

            int replyId = topicService.saveReply(userId, topicId, replyContent);
            System.out.print("success insert replyId = " + replyId + " reply!");
        }
    }

    /**
     * topicId 查询话题详情（基本信息 + 内容 + 回复列表）
     */
    @Test
    public void testGetTopic() throws Exception {
        int topicId = 1;

        Map<String, Object> topicMap = topicService.getTopic(topicId);
        System.out.println(JsonUtil.toJSONStringByObject(topicMap));
    }

    /**
     * replyId 查询回复信息
     */
    @Test
    public void testGetReply() throws Exception {
        int replyId = 1;

        Map<String, Object> replyMap = topicService.getReply(replyId);
        System.out.println(JsonUtil.toJSONStringByObject(replyMap));
    }

    /**
     * 查询话题列表（指定页数与显示数量）
     */
    @Test
    public void testListTopics() throws Exception{
        int page = 10;
        int count = 10;
        List<Map<String, Object>> topics = topicService.listTopics(page, count);

        for(Map map: topics) {
            System.out.println("*************************** topic ****************************");
            System.out.println(JsonUtil.toJSONStringByObject(map));
        }
    }
}
