package test.org.neusoft.neubbs.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 测试所有 DAO 接口
 *
 * @author Suvan
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserDAOTest.class,
        UserActionDAOTest.class,
        UserDynamicDAOTest.class,
        TopicDAOTest.class,
        TopicContentDAOTest.class,
        TopicActionDAOTest.class,
        TopicCategoryDAOTest.class,
        TopicReplyDAOTest.class,
        MessageDAOTest.class
})
public class TestAllDAO { }
