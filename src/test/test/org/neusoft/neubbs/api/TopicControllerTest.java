package test.org.neusoft.neubbs.api;

import com.alibaba.fastjson.JSON;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.exception.TopicErrorException;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicCategoryDAO;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.entity.TopicCategoryDO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Topic api 测试
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class TopicControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ITopicDAO topicDAO;

    @Autowired
    private ITopicContentDAO topicContentDAO;

    @Autowired
    private ITopicCategoryDAO topicCategoryDAO;

    @Autowired
    private ITopicReplyDAO topicReplyDAO;

    @Autowired
    private ITopicService topicService;


    /**
     * 打印成功通过 Test 函数消息
     */
    private void printSuccessPassTestMehtodMessage() {
        //current executing mehtod
        String currentDoingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        System.out.println("*************************** 【"
                + currentDoingMethod + "()】"
                + " pass all the tests ****************************");
    }

    /**
     * 确认结果集 Model Map 应该拥有以下 Key 选项
     *
     * @param map 需要判断的键值对
     * @param keyItems key选项
     * @throws Exception 所有异常
     */
    private void confirmMapShouldHavaKeyItems(Map map, String... keyItems) throws Exception {

        //compare length and judge exist item
        Assert.assertEquals(keyItems.length, map.size());
        Assert.assertThat((Set<String>) map.keySet(), CoreMatchers.hasItems(keyItems));
    }

    /**
     * 获取已经登陆用户 Cookie
     *
     * @return Cookie 已经登录用户Cookie
     */
    public Cookie getAlreadLoginUserCookie() {
        //cookie user, explain alreay login and activated
        UserDO user = new UserDO();
            user.setId(5);
            user.setName("suvan");
            user.setRank("admin");
            user.setState(1);

        return new Cookie(ParamConst.AUTHENTICATION, JwtTokenUtil.createToken(user));
    }

    /**
     * 生成 JSON 字段
     *
     * @param key 字符串键
     * @param value Object值
     * @return String JSON字段
     */
    public String getJsonField(String key, Object value) {
        if (value == null) {
            value = null;
        } else if (value instanceof String) {
            value = "\"" + value + "\"";
        }
        return "\"" + key + "\":" + value;
    }

    @BeforeClass
    public static void init() {
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 【/api/topic (http get)】 test get topic information success
     */
    @Test
    public void testGetTopicInformationSuccess() throws Exception {
        String topicId = "1";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic").param("topicid", topicId)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
            .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model
        Map modelMap = (Map) resultMap.get("model");
        this.confirmMapShouldHavaKeyItems(modelMap,
                "title", "replies", "lastreplytime", "createtime", "content",
                "read", "like", "category", "user", "lastreplyuser", "replys");

        //judge $.model.category
        this.confirmMapShouldHavaKeyItems((Map) modelMap.get("category"), "id", "name");

        //judge $.model.user
        Map modelUserMap = (Map) modelMap.get("user");
        this.confirmMapShouldHavaKeyItems((Map) modelMap.get("user"), "username", "avator");

        //judge $.model.lastreplyuser
        this.confirmMapShouldHavaKeyItems((Map) modelMap.get("lastreplyuser"), "username", "avator");

        //judge $.model.replys
        List modelReplysList = (List) modelMap.get("replys");
        if (modelReplysList.size() > 0) {
            //get first reply map
            Map firstReplyMap = (Map) modelReplysList.get(0);
            this.confirmMapShouldHavaKeyItems(firstReplyMap,
                    "content", "agree", "oppose", "createtime", "replyid", "user");

            //judge $.model.replys[0].user
            this.confirmMapShouldHavaKeyItems( (Map) firstReplyMap.get("user"), "username", "avator");
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic (http get)】 test get topic information throw exception
     *      - request param error, no norm
     *      - database exception
     *          - no topic
     */
    @Test
    public void testGetTopicInformationThrowException() throws Exception{
        //check length, format, database data no exist
        String[] params = {
             null, "1111111111111111", "asdfa*1-", "10000000"
        };

        for (String param: params) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topic").param("topicid", param)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(TopicErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class)
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply (http get)】 test get topic reply information success
     *      - select latest reply
     */
    @Test
    public void testGetTopicReplyInformationSuccess() throws Exception {
        String replyId = String.valueOf(topicReplyDAO.getMaxTopicReplyId());

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic/reply").param("replyid", replyId)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model
        Map modelMap = (Map) resultMap.get("model");
        this.confirmMapShouldHavaKeyItems(modelMap,
                "topicid", "content", "agree", "oppose", "createtime", "user");

        //judge $.model.user
        this.confirmMapShouldHavaKeyItems((Map) modelMap.get("user"), "username", "avator");

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply (http get)】 test get topic reply information throw exception
     *      - request param error, no param
     *      - database exception
     *          - no reply
     */
    @Test
    public void testGetTopicReplyInformationThrowException() throws Exception {
        String[] params = {null, "*12+", "abc", "11111111111", "11123k3k"};

        for (String param: params) {
            System.out.println("input replyid=" + param);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topic/reply").param("replyid", param)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class)
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics】test get topic list basic information by filter params success
     *      - get topic list (pass params: limit, page, category, username)
     *      - if no input limit param, neubbs.properties hava default value
     *      - page topic list desc display
     *
     *      - input param test
     *          - [ ] input page
     *          - [✔] input limit, page
     *          - [ ] input page, category
     *          - [ ] input limit, page category
     *          - [ ] input page, category
     *          - [ ] input limit, page, username
     *          - [ ] input page, username
     *          - [ ] input limit, page, category, username
     *          - [ ] input page, category, username
     *          - [ ] input limit, page, category, username
     */
    @Test
    public void testGetTopicListBasicInformationByFilterParamsSuccess() throws Exception {
        String apiUrl = "/api/topics";

        //input limit, page
        String limit = "1";
        String page = "1";
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl)
                        .param("limit", limit)
                        .param("page", page)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model, because limit=1, so assert list.size()
        List modelList = (List) resultMap.get("model");
        Assert.assertEquals(1, modelList.size());

        Map firstModelListMap = (Map) modelList.get(0);

        this.confirmMapShouldHavaKeyItems(firstModelListMap,
                "title", "replies", "lastreplytime", "createtime", "topicid",
                "content", "read", "like", "category", "user", "lastreplyuser");

        //judge $.mode.category
        this.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("category"), "id", "name");

        //judge $.model.user
        this.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("user"), "username", "avator");

        //$judge $.model.lastreplyuser
        this.confirmMapShouldHavaKeyItems((Map) firstModelListMap.get("lastreplyuser"), "username", "avator");

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics】test get topic list information by page and limit throw exception
     *      - request param eroorn, no norm
     *      - database exception
     *          - [✔] input page exceed topic max page
     *          - [ ] no query topics
     *          - [ ] no category
     *          - [ ] no username
     */
    @Test
    public void testGetTopicListInformationByPageAndLimitThrowException() throws Exception {
        //page and limit, two param
        String[][] params = {
                {null, null}, {"1", null},
                {"1111111111111", "213"}, {"12", "1111111111111"}, {"11111111111111", "1111111111"},
                {"*-+", "1"}, {"1", "-=="}, {"-&&*%", "****("}, {"asdfasdf*123", "___jhjh123"},
                {"10000","1"}
        };

        for (String[] param: params) {
            String limit = param[0];
            String page = param[1];
            System.out.println("input param: limit=" + limit + ", page=" + page);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics")
                                .param("limit", limit)
                                .param("page", page)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(CoreMatchers.is("false")))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(TopicErrorException.class)
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics/pages】test get topic total pages success
     *      - if no input limit param, neubbs.properties hava default value
     *
     *      - input param test
     *          - [✔] no input (default limit = 25)
     *          - [✔] input limit,
     *          - [✔] input category
     *          - [ ] input limit, category
     *          - [✔] input username
     *          - [ ] input limit, username
     *          - [✔] input category, username
     *          - [ ] input limit, category, username
     */
    @Test
    public void testGetTopicTotalPagesSuccess() throws Exception {
        String apiUrl = "/api/topics/pages";

        String limit;
        String category;
        String username;

        //no input, use default limit = 25
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());


        //input limit
        limit = "5";
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl).param("limit", limit)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input category
        category = "game";
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl).param("category", category)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input username
        username = "suvan";
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl).param("username", username)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input category,username
        category = "game";
        username = "suvan";
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiUrl)
                        .param("category", category)
                        .param("username", username)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics/pages】test get topic total pages throw exception
     *      - prompt: if throw exception, will no to do .andExpect(...)
     *
     *  - [ ] reqeust param error, no norm
     *  - [ ] database exception
     *          - [✔] no category
     *          - [✔] no username
     */
    @Test
    public void testGetTopicTotalPagesThrowException() throws Exception {
        String apiUrl = "/api/topics/pages";

        //input category, but category no exist
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get(apiUrl).param("category", "noExistCategory")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_CATEGORY))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof TopicErrorException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_CATEGORY);
        }

        //input username, but username no exist
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get(apiUrl).param("username", "noExistUser")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("asd"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof AccountErrorException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_USER);
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/categorys】 test get topic category list success
     */
    @Test
    public void testGetTopicCategoryListSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic/categorys")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //judge $.model, is list
        List modelList = (List) resultMap.get("model");

        //judge $.model[0], is map
        Map firstModelListCategoryMap = (Map) modelList.get(1);
        this.confirmMapShouldHavaKeyItems(firstModelListCategoryMap, "id", "name");

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic (http post)】 test publish topic success
     *      - get user cookie
     *          - already login
     *          - account activated
     */
    @Test
    @Transactional
    public void testPublishTopicSuccess() throws Exception {
        String categoryNick = "game";
        String title = "new topic title";
        String content = "new topic Contentn";

        String requestBodyJson = "{\"category\":\"" + categoryNick + "\","
                + "\"title\":\"" + title + "\","
                + "\"content\":\"" + content + "\"}";
        System.out.println("input request-body: " + requestBodyJson);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic")
                        .cookie(this.getAlreadLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //jduge $.model.topicid
        Map modelMap = (Map) resultMap.get("model");
        int topicId = (Integer) modelMap.get("topicid");
        Assert.assertTrue(topicId >= 0);
        System.out.println("new insert test topic, topicid = " + topicId);

        //compare database topic information
        TopicDO topic = topicDAO.getTopicById(topicId);
        int categoryId = topic.getCategoryid();
        Assert.assertNotNull(topic);
        Assert.assertEquals(categoryNick, topicCategoryDAO.getTopicCategoryById(categoryId).getNick());
        Assert.assertEquals(title, topic.getTitle());
        Assert.assertEquals(content, topicContentDAO.getTopicContentByTopicId(topicId).getContent());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic (http post)】 test publish topic throw exception
     *      - permission exception
     *          - [✔] no login
     *          - [ ] account no activate
     *      - reqest param error, no norm
     *          - [✔] null check
     *          - [ ] param norm
     *              - category nick no is pure english
     *      - database exception
     *          - [ ] no user (get user from cookie)
     *          - [ ] no category
     */
    @Test
    public void testPublishTopicThrowException() throws Exception {
        String apiUrl = "/api/topic";

        //no login
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post(apiUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage. NO_PERMISSION));

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof AccountErrorException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_PERMISSION);
        }

        //reqest param error, no norm
        String category = "test category";
        String title = "test title";
        String content = "test content";
        String[][] params = {
                {null, null, null}, {null, title, content}, {category, null, content}, {category, title, null},
                {"moive 123", title, content}
        };

        for (String[] param: params) {
            String requestBody = "{" + this.getJsonField("category", param[0]) + ","
                    + this.getJsonField("title", param[1]) + ","
                    + this.getJsonField("content", param[2]) + "}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post(apiUrl)
                                .cookie(this.getAlreadLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(), CoreMatchers.instanceOf(ParamsErrorException.class));
            }
        }


        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply (http post)】 test publish topic reply success
     */
    @Test
    @Transactional
    public void testPublishTopicReplySuccess() throws Exception {
        int topicId = 1;
        String content = "new reply content";
        String requestBodyJson = "{" + this.getJsonField("topicid", topicId) + ","
                + this.getJsonField("content", content) + "}";
        System.out.println("input request-body: " + requestBodyJson);

        //before publish reply
        TopicDO beforeTopic = topicDAO.getTopicById(topicId);
        Assert.assertNotNull(beforeTopic);
        Integer beforeTopicReplies = beforeTopic.getReplies();
        Integer beforeTopicLastUserId = beforeTopic.getLastreplyuserid();
        Date beforeTopicLastReplyTime = beforeTopic.getLastreplytime();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());

        //$.model.replyid
        Map modelMap = (Map) resultMap.get("model");
        int replyId = (Integer) modelMap.get("replyid");

        //copare databse reply information
        TopicReplyDO reply = topicReplyDAO.getTopicReplyById(replyId);

        Assert.assertEquals(topicId, (int) reply.getTopicid());
        Assert.assertEquals(content, reply.getContent());

        TopicDO afterTopic = topicDAO.getTopicById(reply.getTopicid());
        Integer afterReplies = afterTopic.getReplies();
        Integer afterLastReplyUserId = afterTopic.getLastreplyuserid();
        Date afterLastReplyTime = afterTopic.getLastreplytime();

        Assert.assertEquals(beforeTopicReplies + 1, (int) afterReplies);
        Assert.assertNotEquals(beforeTopicLastUserId, afterLastReplyUserId);
        Assert.assertNotEquals(beforeTopicLastReplyTime, afterLastReplyTime);

        Assert.assertEquals(reply.getCreatetime(), afterLastReplyTime);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【undone】
     * 【/api/topic/reply (http post)】 test publish topic reply throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     *          - [ ] no user (get user from cookie)
     */

    /**
     * 【/api/topic-remove】test remove topic success
     */
    @Test
    @Transactional
    public void testRemoveTopicSuccess() throws Exception {
        int topicId = 1;
        String requestBodyJson = "{" + this.getJsonField("topicid", topicId) + "}";
        System.out.println("input reqeust-body: " + requestBodyJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic-remove")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //confirm database already remove topic related (topic information, reply)
        Assert.assertNull(topicDAO.getTopicById(topicId));
        Assert.assertNull(topicContentDAO.getTopicContentByTopicId(topicId));
        List<TopicReplyDO> topicReplyList = topicReplyDAO.listTopicReplyByTopicId(topicId);
        Assert.assertEquals(topicReplyList.size(), 0);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【undone】
     * 【/api/topic-remove】test remove topic throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *          - [ ] rank is 'admin'
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     */

    /**
     * 【/api/topic-remove】test remove topic reply success
     */
    @Test
    @Transactional
    public void testRemoveTopicReplySuccess() throws Exception {
        int replyId = 43;
        String requestBodyJson = "{" + this.getJsonField("replyid", replyId) + "}";
        System.out.println("input reqeust-body: " + requestBodyJson);

        TopicReplyDO beforeReply = topicReplyDAO.getTopicReplyById(replyId);
        Assert.assertNotNull(beforeReply);
        TopicDO beforeTopic = topicDAO.getTopicById(beforeReply.getTopicid());
        Integer beforeTopicReplies = beforeTopic.getReplies();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply-remove")
                        .cookie(this.getAlreadLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //confirm database already remove reply and related topic replies-1
        Assert.assertNull(topicReplyDAO.getTopicReplyById(replyId));

        int beforeTopicId = beforeTopic.getId();
        TopicDO afterTopic = topicDAO.getTopicById(beforeTopicId);
        Integer afterTopicReplies = afterTopic.getReplies();
        Assert.assertEquals(beforeTopicReplies - 1, (int) afterTopicReplies);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【undone】
     * 【/api/topic/reply-remove】test remove topic reply throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *          - [ ] rank is 'admin'
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     */

    /**
     * 【/api/topic-update】test update topic success
     */
    @Test
    @Transactional
    public void testUpdateTopicSuccess() throws Exception {
        int topicId = 1;
        String newCategoryNick = "school";
        String newTitle = "new title";
        String newContent = "new content";

        String requestBodyJson = "{" + this.getJsonField("topicid", topicId) + ","
                + this.getJsonField("category", newCategoryNick) + ","
                + this.getJsonField("title", newTitle) + ","
                + this.getJsonField("content", newContent) + "}";
        System.out.println("input request-body: " + requestBodyJson);

         mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic-update")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //compare database data, confirm already alter
        TopicDO afterTopic = topicDAO.getTopicById(topicId);
        TopicContentDO afterTopicContent = topicContentDAO.getTopicContentByTopicId(topicId);
        TopicCategoryDO afterTopicCategory = topicCategoryDAO.getTopicCategoryById(afterTopic.getCategoryid());
        String afterCategory = afterTopicCategory.getNick();
        String afterTitle = afterTopic.getTitle();
        String afterContent = afterTopicContent.getContent();

        Assert.assertEquals(newCategoryNick, afterCategory);
        Assert.assertEquals(newTitle, afterTitle);
        Assert.assertEquals(newContent, afterContent);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【undone】
     * 【/api/topic-update】test update topic throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *          - [ ] rank is 'admin'
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     */

    /**
     * 【/api/topic/reply-update】test update topic reply success
     */
    @Test
    @Transactional
    public void testUpdateTopicReplySuccess() throws Exception {
        int replyId = 43;
        String content = "new reply content";
        String requestBodyJson = "{" + this.getJsonField("replyid", replyId) + ","
                + this.getJsonField("content", content) + "}";
        System.out.println("input reqeust-body: " + requestBodyJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply-update")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //compare database data, confirm already alter
        TopicReplyDO beforeReply = topicReplyDAO.getTopicReplyById(replyId);
        Assert.assertEquals(content, beforeReply.getContent());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply-update】test update topic reply throw exception
     *      - permission exception
     *          - [ ] no login
     *          - [ ] account no activate
     *          - [ ] rank is 'admin'
     *      - request param error, no norm
     *          - [ ] null
     *          - [ ] param norm
     *      - database exception
     *          - [ ] no topic
     */
}
