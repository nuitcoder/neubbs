package test.org.neusoft.neubbs.util;

import org.junit.Assert;
import org.junit.Test;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.StringUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * StringUtil 测试类
 *      - 测试 isEmpty()
 *      - 测试 isScope()
 *      - 测试 isExpire()
 *      - 测试 generateActivateMailHtmlContent()
 *      - 测试 generateUserAvatarUrl()
 *      - 测试 getTodayTwentyFourClockTimestamp()
 *      - 测试 judgeSeparateDay()
 *      - 测试 completeAroundSprit()
 *
 * @author Suvan
 */
public class StringUtilTest {

    /**
     * 测试 isEmpty()
     */
    @Test
    public void testIsEmpty() {
        Assert.assertTrue(StringUtil.isEmpty(""));
        Assert.assertTrue(StringUtil.isEmpty(null));

        Assert.assertFalse(StringUtil.isEmpty("param"));
        Assert.assertFalse(StringUtil.isEmpty("null"));
    }

    /**
     * 测试 isScope()
     */
    @Test
    public void testIsScope() {
        Assert.assertTrue(StringUtil.isScope("param", 0, 10));
        Assert.assertTrue(StringUtil.isScope("param", 4, 5));

        Assert.assertFalse(StringUtil.isScope("param", 10, 20));
        Assert.assertFalse(StringUtil.isScope("param", 1, 3));
    }

    /**
     * 测试 testIsExpire()
     */
    @Test
    public void isExpire() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(2015, Calendar.JANUARY, 23, 12, 30, 35);
        Assert.assertTrue(StringUtil.isExpire(String.valueOf(calendar.getTimeInMillis())));

        calendar.set(2019, Calendar.DECEMBER, 31);
        Assert.assertFalse(StringUtil.isExpire(String.valueOf(calendar.getTimeInMillis())));
    }

    /**
     * 测试 generateActivateMailHtmlContent()
     */
    @Test
    public void testGenerateActivateMailHtmlContent() {
        String receiveEmail = "liushuwei0925@gmail.com";
        String activateUrl = "http://localhost:8080/account/validate?token=";
        String token = SecretUtil.encodeBase64(receiveEmail + "-" + StringUtil.getTodayTwentyFourClockTimestamp());
        String url = activateUrl + token;

        System.out.println(StringUtil.generateActivateMailHtmlContent(url));
    }

    /**
     * 测试 generateUserAvatarUrl()
     */
    @Test
    public void testGenerateUserAvatarUrl() {
        UserDO user = new UserDO();
            user.setId(5);
            user.setName("suvan");
            user.setAvator(SetConst.USER_DEFAULT_AVATAR);

        //default user avatar
        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(user);
        String defaultUserAvatarUrl = StringUtil.generateUserAvatarUrl(userInfoMap);
        Assert.assertEquals(
                "http://119.29.192.62/user/default/" + SetConst.USER_DEFAULT_AVATAR,
                defaultUserAvatarUrl
        );
        System.out.println(defaultUserAvatarUrl);

        //user upload, custom user avatar
        user.setAvator("my-picture.png");
        userInfoMap = JsonUtil.toMapByObject(user);
        String customUserAvatarUrl = StringUtil.generateUserAvatarUrl(userInfoMap);
        Assert.assertEquals(
                "http://119.29.192.62/user/" + user.getId() + "-" + user.getName() + "/avator/" + user.getAvator(),
                customUserAvatarUrl
        );
        System.out.println(customUserAvatarUrl);
    }

    /**
     * 测试 getTodayTwentyFourClockTimestamp()
     */
    @Test
    public void testGetTodayTwentyFourClockTimestamp() {
        Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 24);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

        Assert.assertEquals(
                String.valueOf(calendar.getTimeInMillis()),
                StringUtil.getTodayTwentyFourClockTimestamp()
        );

        System.out.println("today 24:00 = " + calendar.getTimeInMillis());
    }

    /**
     * 测试 judgeSeparateDay()
     */
    @Test
    public void testJudgeSeparateDay() throws ParseException {
        //today
        Date today = new Date();
        Assert.assertEquals("今天", StringUtil.judgeSeparateDay(today.getTime(), System.currentTimeMillis()));

        //past time = 3 day age
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 3);
        Assert.assertEquals("3 天前", StringUtil.judgeSeparateDay(today.getTime(), calendar.getTimeInMillis()));
    }

    /**
     * 测试 completeAroundSprit()
     */
    @Test
    public void testCompleteAroundSprit() {
        String[] arrayStr = {"/hello world", "hello world/", "hello world", "/hello world/"};

        for (String str: arrayStr) {
            Assert.assertEquals("/hello world/", StringUtil.completeAroundSprit(str));
        }
    }
}
