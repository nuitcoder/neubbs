package test.org.neusoft.neubbs.api;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.ITopicContentDAO;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.entity.TopicContentDO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.entity.TopicReplyDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    private ITopicReplyDAO topicReplyDAO;



    /**
     * 打印成功通过 Test 函数消息
     */
    public void printSuccessPassTestMehtodMessage() {
        //current executing mehtod
        String currentDoingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        System.out.println("*************************** 【"
                + currentDoingMethod + "()】"
                + " pass all the tests ****************************");
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
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic").param("topicid", topicId)
        ).andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.model").exists())
            .andExpect(jsonPath("$.model.topicid").value(Integer.parseInt(topicId)))
            .andExpect(jsonPath("$.model.user").exists())
            .andExpect(jsonPath("$.model.lastreplyuser").exists())
            .andExpect(jsonPath("$.model.replys").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic (http get)】 test get topic information throw exception
     *      - request param check
     *      - database exception
     *          - no topic
     */
    @Test
    public void testGetTopicInformationThrowException() throws Exception{
        //check length, format, data exist
        String[] params = {
             null, "1111111111111111", "asdfa*1-", "10000"
        };

        for (String param: params) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topic").param("topicid", param)
                ).andExpect(jsonPath("$.success").value(false))
                 .andExpect(jsonPath("$.message").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(TopicErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class)));
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply (http get)】 test get topic reply information success
     */
    @Test
    public void testGetTopicReplyInformationSuccess() throws Exception {
        String replyId = "1";

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic/reply")
                    .param("replyid", replyId)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.message").value(""))
         .andExpect(jsonPath("$.model").exists());

        String model = "$.model.";
        result.andExpect(jsonPath(model + "content").exists())
                .andExpect(jsonPath(model + "agree").exists())
                .andExpect(jsonPath(model + "oppose").exists())
                .andExpect(jsonPath(model + "createtime").exists())
                .andExpect(jsonPath(model + "replyid").value(Integer.parseInt(replyId)))
                .andExpect(jsonPath(model + "user.avator").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath(model + "user.username").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply (http get)】 test get topic reply information throw exception
     *      - request param check
     *      - database exception
     *          - no reply
     */
    @Test
    public void testGetTopicReplyInformationThrowException() throws Exception {
        String[] params = {null, "*12+", "abc", "11111111111", "11123k3k"};

        for (String param: params) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topic/reply")
                            .param("replyid", param)
                ).andExpect(jsonPath("$.success").value(false))
                 .andExpect(jsonPath("$.message").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class))
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics】test get topic list information by page and limit success
     *      - the optional param limit, neubbs.properties hava default value
     *      - input limit,page
     *      - input limit,page,category,username (no finish)
     */
    @Test
    public void testGetTopicListInformationByPageAndLimitSuccess() throws Exception {
        //only input limit and page
        int[][] params = {
                {20, 3}, {2, 20}, {43, 2}, {3}
        };

        for (int[] param: params) {
            String limit;
            String page;
            ResultActions result;
            if (param.length == 1) {
                page = String.valueOf(param[0]);
                System.out.print("input param[ page=" + page + " ]");

                result = mockMvc.perform(MockMvcRequestBuilders.get("/api/topics").param("page", page));
            } else {
                limit = String.valueOf(param[0]);
                page = String.valueOf(param[1]);
                System.out.print("input param[ limit=" + limit + "; page=" + page + " ]");

                result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics")
                                .param("limit", limit)
                                .param("page", page)
                );
            }

            result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            //get model topic list
            Map<String, Object> rootMap = JsonUtil.toMapByJSONString(result.andReturn().getResponse().getContentAsString());
            ArrayList<Map<String, Object>> modelList = (ArrayList<Map<String, Object>>)rootMap.get("model");
            Map<String, Object> firstModelMap = modelList.get(0);

            //model topic map exist assign key item,$.model[0]
            Assert.assertThat(firstModelMap.keySet(),
                    CoreMatchers.hasItems("category","title","replies", "createtime","topicid",
                            "read", "agree","user", "lastreplyuser")
            );

            //$.model[0].user
            Map<String, Object> userMap = (Map<String, Object>) firstModelMap.get("user");
            Assert.assertThat(userMap.keySet(),
                    CoreMatchers.allOf(
                        CoreMatchers.hasItem("avator"),CoreMatchers.hasItem("username")
                    )
            );

            //$.model[0].lastreplyuser
            Map<String, Object> lastReplyUserMap = (Map<String, Object>) firstModelMap.get("lastreplyuser");
            Assert.assertArrayEquals(
                    lastReplyUserMap.keySet().toArray(), new String[]{"username", "avator"}
            );

        //input param: limit,page,category
        String category = "分类 2";
        System.out.println("input param[ limit=1, page=1,category=" + category + "]");
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/topics")
                .param("limit", "1")
                .param("page", "1")
                .param("category", category)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model[0].category").doesNotExist());

        //input param: limit,page,username
        String username = "suvan";
        System.out.println("input param[ limit=1, page=1,username=" + username + "]");
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics")
                    .param("limit", "1")
                    .param("page", "1")
                    .param("username", "suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model[0].user.username").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model[0].user.avator").exists());

            //the same input category and username
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics")
                                .param("page", "1")
                                .param("limit", "1")
                                .param("category", "fasdf")
                                .param("username", "asdf")
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""));
            } catch (NestedServletException ne) {
                Assert.assertTrue(ne.getRootCause() instanceof TopicErrorException);
            }

            System.out.println("\t<test result: success!>");
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics】test get topic list information by page and limit throw exception
     *      - request param check
     *          -  if no input limit, neubbs.properties hava default value
     */
    @Test
    public void testGetTopicListInformationByPageAndLimitThrowException() throws Exception {
        //page and limit, two param
        String[][] params = {
                {null, null}, {"1", null},
                {"1111111111111", "213"}, {"12", "1111111111111"}, {"11111111111111", "1111111111"},
                {"*-+", "1"}, {"1", "-=="}, {"-&&*%", "****("}, {"asdfasdf*123", "___jhjh123"},
                {"10000","1", "23", "10000", "10000", "10000"}
        };

        for (String[] param: params) {
            String limit = param[0];
            String page = param[1];
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics")
                            .param("limit", limit).param("page", page)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(CoreMatchers.is("false")))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.nullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(TopicErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class)
                        )
                );
            }
        }

        //only page param
        String[] pages = {
                null, "***", "asdfasd", "*fasf", "asdfa123**(", "*123",
                "11111111111111111",
                "10000"
        };
        for (String page: pages) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/topics").param("page", page)
                ).andExpect(jsonPath("$.success").value(false))
                 .andExpect(jsonPath("$.message").exists())
                 .andExpect(jsonPath("$.model").doesNotExist());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        anyOf(instanceOf(ParamsErrorException.class),
                                instanceOf(TopicErrorException.class),
                                instanceOf(AccountErrorException.class)
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics/pages】test get topic total pages success
     *      - request param check
     *        - no input
     *        - input limit
     *        - input category
     *        - input limit,category
     *        - input username
     *        - input limit,username
     *        - input category,username
     *        - input limit,category,username
     */
    @Test
    public void testGetTopicTotalPagesSuccess() throws Exception {
        //no input, use default limit = 25
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/pages")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input limit
        String limit = "5";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/pages")
                    .param("limit", limit)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input category
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/pages")
                    .param("category", "分类 1")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input limit,category
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/pages")
                        .param("limit", limit)
                        .param("category", "分类 1")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input username
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/pages")
                    .param("username", "suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input limit,username
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/pages")
                        .param("limit", limit)
                        .param("username", "suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input category,username
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topics/pages")
                        .param("category", "分类 1")
                        .param("username", "suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.totalpages").exists());

        //input limit,category,username(wait realize function)

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topics/pages】test get topic total pages throw exception
     *  - reqeust param check
     */
    @Test
    public void testGetTopicTotalPagesThrowException() throws Exception {
        //input category, but category no exist
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/topics/pages")
                            .param("category", "no category")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof TopicErrorException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_TOPIC_CATEGORY);
        }

        //input username, but username no exist
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/topics/pages")
                        .param("username", "existUser")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
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
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/topic/categorys")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.categorys").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic (http post)】 test publish topic success
     */
    @Test
    @Transactional
    public void testPublishTopicSuccess() throws Exception {
        String category = "new Category";
        String title = "new topic title";
        String contennt = "new topic Contentn";

        String requestBodyJson = "{\"category\":\"" + category + "\"," +
                "\"title\":\"" + title + "\"," +
                "\"content\":\"" + contennt + "\"}";

        String pageJsonContent = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic")
                        .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.topicid").exists())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> model = (Map<String, Object>) JsonUtil.toMapByJSONString(pageJsonContent).get("model");
        int topicId = (int)model.get("topicid");
        System.out.println("new publish topicid = " + topicId);

        //database check
        TopicDO topic = topicDAO.getTopicById(topicId);
        Assert.assertNotNull(topic);
        Assert.assertEquals(topic.getCategory(), category);
        Assert.assertEquals(topic.getTitle(), title);

        TopicContentDO topicContent = topicContentDAO.getTopicContentById(topicId);
        Assert.assertNotNull(topicContent);
        Assert.assertEquals(topicContent.getContent(), contennt);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic (http post)】 test publish topic throw exception
     *      - no login
     *      - reqest param check
     */
    @Test
    public void testPublishTopicThrowException() throws Exception {
        //no login
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/topic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof AccountErrorException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_PERMISSION);
        }

        //already login,param check test
        String category = "new category";
        String title = "new title";
        String content = "new content";
        String[][] params = {
                {null, null, null}, {null, title, content}, {category, null, content}, {category, title, null},
        };

        for (String[] param: params) {
            String requestBody = "{" + this.getJsonField("category", param[0]) + ","
                    + this.getJsonField("title", param[1]) + ","
                    + this.getJsonField("content", param[2]) + "}";
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/topic")
                                .cookie(this.getAlreadLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());
            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(DatabaseOperationFailException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class)
                        )
                );
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

        //insert before topic comment
        int beforeComment = Integer.parseInt(topicDAO.getTopicById(topicId).getComment());

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""));

        String resultJson = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> resultMap = JsonUtil.toMapByJSONString(resultJson);
        Map<String, Object> modelMap = (Map<String, Object>) resultMap.get("model");
        int replyId = (int) modelMap.get(ParamConst.REPLY_ID);

        //confirm already insert reply
        TopicReplyDO replyDO = topicReplyDAO.getTopicReplyById(replyId);
        Assert.assertNotNull(replyDO);

        //confirm topic update comment,lastreplyuserid,lastreplytime
        TopicDO topic = topicDAO.getTopicById(topicId);
        int afterComment = Integer.parseInt(topic.getComment());
        Assert.assertTrue(beforeComment + 1 == afterComment);
        Assert.assertEquals(topic.getLastreplyuserid(), replyDO.getUserid());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply (http post)】 test publish topic reply throw exception
     *      - no login
     *      - request param check
     *      - database exception
     */

    /**
     * 【/api/topic-remove】test remove topic success
     */
    @Test
    @Transactional
    public void testRemoveTopicSuccess() throws Exception {
        int topicId = 1;
        String requestBodyJson = "{" + this.getJsonField("topicid", topicId) + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic-remove")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""));

        //database data check, judge whether already delete topic and related content reply
        Assert.assertNull(topicDAO.getTopicById(topicId));
        Assert.assertNull(topicContentDAO.getTopicContentById(topicId));
        List<TopicReplyDO> topicReplyList = topicReplyDAO.listTopicReplyByTopicId(topicId);
        Assert.assertEquals(topicReplyList.size(), 0);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic-remove】test remove topic throw exception
     *      - no login
     *      - account no activated
     *      - account no 'admin' rank
     *      - request param check
     *      - database exception
     */

    /**
     * 【/api/topic-update】test update topic success
     */
    @Test
    @Transactional
    public void testUpdateTopicSuccess() throws Exception {
        int topicId = 1;
        String category = "new category";
        String title = "new title";
        String content = "new content";

        String requestBodyJson = "{" + this.getJsonField("topicid", topicId) + ","
                + this.getJsonField("category", category) + ","
                + this.getJsonField("title", title) + ","
                + this.getJsonField("content", content) + "}";

        TopicDO beforeTopic = topicDAO.getTopicById(topicId);
        TopicContentDO beforeTopicContent = topicContentDAO.getTopicContentById(topicId);

         mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic-update")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""));

        TopicDO afterTopic = topicDAO.getTopicById(topicId);
        TopicContentDO afterTopicContent = topicContentDAO.getTopicContentById(topicId);

        //database data check, confirm alrady alter
        Assert.assertNotEquals(beforeTopic.getCategory(), afterTopic.getCategory());
        Assert.assertNotEquals(beforeTopic.getTitle(), afterTopic.getTitle());
        Assert.assertNotEquals(beforeTopicContent.getContent(), afterTopicContent.getContent());

        Assert.assertEquals(category, afterTopic.getCategory());
        Assert.assertEquals(title, afterTopic.getTitle());
        Assert.assertEquals(content, afterTopicContent.getContent());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic-update】test update topic throw exception
     *      - no login
     *      - account no activated
     *      - request param check
     *      - database exception
     */

    /**
     * 【/api/topic/reply-update】test update topic reply success
     */
    @Test
    @Transactional
    public void testUpdateTopicReplySuccess() throws Exception {
        int replyId = 1;
        String content = "new reply content";

        String requestBodyJson = "{" + this.getJsonField("replyid", replyId) + ","
                + this.getJsonField("content", content) + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/topic/reply-update")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""));

        //database data check, confirm already alter
        TopicReplyDO reply = topicReplyDAO.getTopicReplyById(replyId);
        Assert.assertEquals(content, reply.getContent());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/topic/reply-update】test update topic reply throw exception
     *      - no login
     *      - account no activated
     *      - request param check
     *      - database exception
     */
}
