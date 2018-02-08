package test.org.neusoft.neubbs.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.entity.TopicCategoryDO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.MapFilterUtil;
import org.neusoft.neubbs.utils.StringUtil;

import java.util.Map;

/**
 * MapFilterUtil 测试类
 *      - 测试 filterUserInfo()
 *      - 测试 filterTopicInfo()
 *      - 测试 filterTopicContentInfo()
 *      - 测试 filterTopicCategory()
 *      - 测试 filterTopicUserInfo()
 *      - 测试 filterTopicReply()
 *      - 测试 generateMap()
 *
 * @author Suvan
 */
@RunWith(JUnit4.class)
public class MapFilterUtilTest {

    /**
     *  测试 filterUserInfo()
     */
    @Test
    public void testFilterUserInfo() {
        UserDO user = new UserDO();
            user.setId(1);
            user.setName("hello");
            user.setPassword("123456");
            user.setAvator("myAvatar.jpg");
            user.setRank("user");

        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(user);
        String beforeAvatar = StringUtil.generateUserAvatarUrl(userInfoMap);

        System.out.println("filter before: " + userInfoMap);
        MapFilterUtil.filterUserInfo(userInfoMap);
        System.out.println("filter after: " + userInfoMap);

        Assert.assertNotNull(userInfoMap);
        Assert.assertNull(userInfoMap.get(ParamConst.ID));
        Assert.assertNull(userInfoMap.get(ParamConst.NAME));
        Assert.assertNull(userInfoMap.get(ParamConst.PASSWORD));
        Assert.assertNull(userInfoMap.get(ParamConst.RANK));
        Assert.assertNotNull(userInfoMap.get(ParamConst.USER_ID));
        Assert.assertEquals(beforeAvatar, userInfoMap.get(ParamConst.AVATOR));
    }

    /**
     * 测试 filterTopicInfo()
     */
    @Test
    public void testFilterTopicInfo() {
        TopicDO topic = new TopicDO();
            topic.setId(1);
            topic.setUserid(1);
            topic.setCategoryid(1);
            topic.setLastreplyuserid(2);

        Map<String, Object> topicInfoMap = JsonUtil.toMapByObject(topic);

        System.out.println("filter before: " + topicInfoMap);
        MapFilterUtil.filterTopicInfo(topicInfoMap);
        System.out.println("filter after: " + topicInfoMap);

        Assert.assertNotNull(topicInfoMap);
        Assert.assertNull(topicInfoMap.get(ParamConst.ID));
        Assert.assertNull(topicInfoMap.get(ParamConst.USER_ID));
        Assert.assertNull(topicInfoMap.get(ParamConst.CATEGORY_ID));
        Assert.assertNull(topicInfoMap.get(ParamConst.LAST_REPLY_USER_ID));
        Assert.assertNotNull(topicInfoMap.get(ParamConst.TOPIC_ID));
    }

    /**
     * 测试 filterTopicContentInfo()
     */
    @Test
    public void testFilterTopicContentInfo() {
        TopicContentDO topicContent = new TopicContentDO();
            topicContent.setId(1);
            topicContent.setTopicid(1);

        Map<String, Object> topicContentInfoMap = JsonUtil.toMapByObject(topicContent);

        System.out.println("filter before: " + topicContentInfoMap);
        MapFilterUtil.filterTopicContentInfo(topicContentInfoMap);
        System.out.println("filter after: " + topicContentInfoMap);

        Assert.assertNotNull(topicContentInfoMap);
        Assert.assertNull(topicContentInfoMap.get(ParamConst.ID));
        Assert.assertNull(topicContentInfoMap.get(ParamConst.ID));
    }

    /**
     * 测试 filterTopicCategory()
     */
    @Test
    public void testFilterTopicCategory() {
        TopicCategoryDO topicCategory = new TopicCategoryDO();
            topicCategory.setId(1);
            topicCategory.setNick("music");

        Map<String, Object> topicCategoryInfoMap = JsonUtil.toMapByObject(topicCategory);

        System.out.println("filter before: " + topicCategoryInfoMap);
        MapFilterUtil.filterTopicCategory(topicCategoryInfoMap);
        System.out.println("filter after: " + topicCategoryInfoMap);

        Assert.assertNotNull(topicCategoryInfoMap);
        Assert.assertNull(topicCategoryInfoMap.get(ParamConst.NICK));
        Assert.assertNotNull(topicCategoryInfoMap.get(ParamConst.ID));
    }

    /**
     * 测试 filterTopicUserInfo()
     */
    @Test
    public void testFilterTopicUserInfo() {
        UserDO user = new UserDO();
            user.setId(1);
            user.setName("hello");
            user.setAvator("myAvatar.png");

        Map<String, Object> topicUserInfoMap = JsonUtil.toMapByObject(user);
        String beforeAvatar = StringUtil.generateUserAvatarUrl(topicUserInfoMap);

        System.out.println("filter before: " + topicUserInfoMap);
        MapFilterUtil.filterTopicUserInfo(topicUserInfoMap);
        System.out.println("filter after: " + topicUserInfoMap);

        Assert.assertNotNull(topicUserInfoMap);
        for(Map.Entry entry: topicUserInfoMap.entrySet()) {
            if (ParamConst.USERNAME.equals(entry.getKey()) || ParamConst.AVATOR.equals(entry.getKey())) {
                Assert.assertNotNull(entry.getValue());
                continue;
            }

            Assert.assertNull(entry.getValue());
        }
        Assert.assertEquals(beforeAvatar, topicUserInfoMap.get(ParamConst.AVATOR));
    }

    /**
     * 测试 filterTopicReply()
     */
    @Test
    public void testFilterTopicReply() {
        TopicReplyDO topicReply = new TopicReplyDO();
            topicReply.setId(1);
            topicReply.setUserid(1);

        Map<String, Object> topicReplyInfoMap = JsonUtil.toMapByObject(topicReply);

        System.out.println("filter before: " + topicReplyInfoMap);
        MapFilterUtil.filterTopicReply(topicReplyInfoMap);
        System.out.println("filter after: " + topicReplyInfoMap);

        Assert.assertNotNull(topicReplyInfoMap);
        Assert.assertNull(topicReplyInfoMap.get(ParamConst.ID));
        Assert.assertNull(topicReplyInfoMap.get(ParamConst.USER_ID));
        Assert.assertNotNull(topicReplyInfoMap.get(ParamConst.REPLY_ID));
    }

    /**
     * 测试 generateMap()
     */
    @Test
    public void testGenerateMap() {
        Map<String, Object> map = MapFilterUtil.generateMap("key", "value");
        System.out.println("generate one size map: " + map);
    }
}
