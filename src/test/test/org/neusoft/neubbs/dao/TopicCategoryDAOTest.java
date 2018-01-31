package test.org.neusoft.neubbs.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicCategoryDAO;
import org.neusoft.neubbs.entity.TopicCategoryDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 测试 ITopicCategoryDAO 接口
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TopicCategoryDAOTest {

    @Autowired
    private ITopicCategoryDAO topicCategoryDAO;

    /**
     * 保存话题分类至数据库
     *
     * @return TopicContent 数据库保存后，重新查询的话题分类
     */
    private TopicCategoryDO savaTestTopicCategoryDOToDatabase() {
        //build TopicCategoryDO, sava database
        TopicCategoryDO category = new TopicCategoryDO();
            category.setNick("testCategory");
            category.setName("测试分类");

        Assert.assertEquals(1, topicCategoryDAO.saveTopicCategory(category));

        return topicCategoryDAO.getTopicCategoryById(category.getId());
    }


    @BeforeClass
    public static void init() {
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 测试保存保存话题分类
     */
    @Test
    @Transactional
    public void testSaveTopicCategory() {
        TopicCategoryDO category = this.savaTestTopicCategoryDOToDatabase();
        System.out.println("insert topic category information: " + category);
    }

    /**
     * 测试删除话题分类
     */
    @Test
    @Transactional
    public void testRemoveTopicCategoryById() {
        TopicCategoryDO category = this.savaTestTopicCategoryDOToDatabase();

        int categoryId = category.getId();
        Assert.assertEquals(1, topicCategoryDAO.removeTopicCategoryById(categoryId));
        Assert.assertNull(topicCategoryDAO.getTopicCategoryById(categoryId));

        System.out.println("delete categoryid=" + categoryId + " topic category");
    }

    /**
     * 测试（id）获取话题分类
     */
    @Test
    @Transactional
    public void testGetTopicCategoryById() {
        TopicCategoryDO category = this.savaTestTopicCategoryDOToDatabase();

        int categoryId = category.getId();
        Assert.assertNotNull(topicCategoryDAO.getTopicCategoryById(categoryId));

        System.out.println("get topic category information: " + topicCategoryDAO.getTopicCategoryById(categoryId));
    }

    /**
     * 测试（昵称）获取话题分类
     */
    @Test
    @Transactional
    public void testGetTopicCategoryByNick() {
        TopicCategoryDO category = this.savaTestTopicCategoryDOToDatabase();

        String categoryNick = category.getNick();
        Assert.assertNotNull(topicCategoryDAO.getTopicCategoryByNick(categoryNick));

        System.out.println("get topic category information: " + topicCategoryDAO.getTopicCategoryByNick(categoryNick));
    }

    /**
     * 测试（name）获取话题分类
     */
    @Test
    @Transactional
    public void testGetTopicCategoryByName() {
        TopicCategoryDO category = this.savaTestTopicCategoryDOToDatabase();

        String categoryName = category.getName();
        Assert.assertNotNull(topicCategoryDAO.getTopicCategoryByName(categoryName));

        System.out.println("get topic category information: " + topicCategoryDAO.getTopicCategoryByName(categoryName));
    }

    /**
     * 测试获取所有话题分类
     */
    @Test
    @Transactional
    public void testListAllTopicCategory() {
        this.savaTestTopicCategoryDOToDatabase();

        List<TopicCategoryDO> categoryList = topicCategoryDAO.listAllTopicCategory();
        Assert.assertTrue(categoryList.size() >= 1);

        int categoryCount = 1;
        for (TopicCategoryDO topicCategory: categoryList) {
            System.out.println("output topic category information ( No." + (categoryCount++) + " records):"
                    + JsonUtil.toJSONString(topicCategory));
        }

        System.out.println("get all category list success!");
    }

    /**
     * 测试修改话题分类描述
     */
    @Test
    @Transactional
    public void testUpdateDescriptionByNick() {
        TopicCategoryDO category = this.savaTestTopicCategoryDOToDatabase();

        String newDescription = "this is new category description";
        Assert.assertEquals(1, topicCategoryDAO.updateDescriptionByNick(category.getNick(), newDescription));

        Assert.assertEquals(newDescription, topicCategoryDAO.getTopicCategoryById(category.getId()).getDescription());

        System.out.println("update categoryNick=" + category.getNick()
                + " category description=<" + newDescription + ">");
    }
}
