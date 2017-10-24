package test.org.neusoft.neubbs.db;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.RandomUtil;
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
        boolean result = topicService.saveTopic(2, "java", "第一个标题哟","话题内容");
        System.out.println("结果：" + result);
    }

    /**
     * 保存话题回复
     */
    @Test
    public void test_12_SaveTopicReply(){
        boolean result = topicService.saveReply(1,5,"第一条回复内容哟");
        System.out.println("结果：" + result);
    }

    /**
     * 保存 10 条话题内容 和 100 条回复，用于测试
     */
    @Ignore
    public void test_13_SaveTopicAndReyply(){
        //保存10条话
        for(int i = 0; i < 10; i++){
            topicService.saveTopic(RandomUtil.getRandomNumberByScope(1, 10),"类别" + i, "标题" + i, "内容" + 1);
            System.out.println("成功生成" + (i+1) + "条话题！");
        }
        System.out.println("10条话题生成成功！");

        //保存100条回复
        for(int i = 0; i < 100; i++){
            topicService.saveReply(RandomUtil.getRandomNumberByScope(1, 10),
                                   RandomUtil.getRandomNumberByScope(1, 10),
                                   "回复内容" + i);
            System.out.println("成功生成" + (i+1) + "条回复！");
        }
        System.out.println("100条回复生成成功！");
    }

    /**
     * 删除话题
     */
    @Test
    public void test_21_RemoveTopic(){
        System.out.println("删除结果：" + topicService.removeTopic(4));
    }

    /**
     * 删除话题回复
     */
    @Test
    public void test_22_RemoveReply(){
        System.out.println("删除结果：" + topicService.removeReply(5));
    }



    /**
     * 修改话题内容
     */
    @Test
    public void test_41_AlterTopicContent(){
        System.out.println("更新结果：" + topicService.alterTopicContent(2, "修改后的topic Content！"));
    }

    /**
     * 修改回复内容
     */
    @Test
    public void test_42_AlterTopicReplyContent(){
        System.out.println("更新结果：" + topicService.alterTopicReplyContent(6, "修改后的 topic Reply Content!") );
    }

}
