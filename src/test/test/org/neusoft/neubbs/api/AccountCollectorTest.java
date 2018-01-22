package test.org.neusoft.neubbs.api;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Producer;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IUserActionDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.exception.TokenErrorException;
import org.neusoft.neubbs.service.ICacheService;
import org.neusoft.neubbs.service.ICaptchaService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.FtpUtil;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import javax.ws.rs.HttpMethod;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Account api 测试
 *      - 声明 Spring
 *      - 开启 web 服务
 *      - 设置配置文件(需设置 applicatonContext.xml 和 mvc.xm,否则会报错)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class AccountCollectorTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IUserActionDAO userActionDAO;

    private final String API_ACCOUNT = "/api/account";
    private final String API_ACCOUNT_STATE = "/api/account/state";
    private final String API_ACCOUNT_LOGIN = "/api/account/login";
    private final String API_ACCOUNT_LOGOUT = "/api/account/logout";
    private final String API_ACCOUNT_REGISTER = "/api/account/register";
    private final String API_ACCOUNT_UPDATE_PASSWORD = "/api/account/update-password";
    private final String API_ACCOUNT_UPDATE_EMAIL = "/api/account/update-email";
    private final String API_ACCOUNT_ACTIVATE = "/api/account/activate";
    private final String API_ACCOUNT_VALIDATE = "/api/account/validate";
    private final String API_ACCOUNT_CAPTCHA = "/api/account/captcha";
    private final String API_ACCOUNT_CHECK_CAPTCHA = "/api/account/check-captcha";
    private final String API_ACCOUNT_FORGET_PASSWORD = "/api/account/forget-password";

    static class Param {
        String key;
        Object value;

        Param(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }


    /**
     * 打印成功通过 Test 函数消息
     */
    public void printSuccessPassTestMehtodMessage() {
        //current executing mehtod
        String currentDoingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        System.out.println("*************************** 【"
                + currentDoingMethod + "()】 "
                + " pass all the tests ****************************");
    }

    /**
     * 获取已经登陆用户 Cookie
     *
     * @return Cookie 已经登录用户Cookie
     */
    public Cookie getAlreadLoginUserCookie() {
        UserDO user = new UserDO();
            user.setId(6);
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

    /**
     * 确认结果集 Model Map 应该拥有以下 Key 选项
     *
     * @param result MvcResult 结果集
     * @param keyItems key选项
     * @throws Exception 所有异常
     */
    private void confirmMvcResultModelMapShouldHavaKeyItems(MvcResult result, String... keyItems) throws Exception {
        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());
        Map modelMap = (Map) resultMap.get("model");

        //compare length and judge exist item
        Assert.assertEquals(keyItems.length, modelMap.size());
        Assert.assertThat((Set<String>) modelMap.keySet(), CoreMatchers.hasItems(keyItems));
    }


    @BeforeClass
    public static void init() {
        //use local MySQL DataBase
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    @Before
    public void setup() {
        //方式1：依赖 Spring 上下文，需加入 @ContextConfiguration 类注解，加载配置文件
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        //方式2: 自主构建控制器，可加入拦截器，过滤器之类
//        StandaloneMockMvcBuilder standBuild = MockMvcBuilders.standaloneSetup(accountController);
//            standBuild.addFilter(new ApiFilter());
//            standBuild.addInterceptors(new ApiInterceptor());
//        this.mockMvc = standBuild.build();
    }

    /**
     * 【/api/account】test no login get user information success
     */
    @Test
    public void testNoLoginGetUserInformationSuccess() throws Exception {
        ArrayList<Param> paramList = new ArrayList<>();
            paramList.add(new Param("username", "suvan"));
            paramList.add(new Param("username", "liushuwei0925@gmail.com"));
            paramList.add(new Param("email", "liushuwei0925@gmail.com"));

        for (Param param : paramList) {
            System.out.println("input: key=" + param.key + ", value=" + param.value);

            mockMvc.perform(
                    MockMvcRequestBuilders.get(API_ACCOUNT)
                            .param(param.key, (String) param.value)
            ).andExpect(MockMvcResultMatchers.status().is(200))
             .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】test already login get user information success
     */
    @Test
    public void testAreadyLoginGetUserInfoSuccess() throws Exception {
        ArrayList<Param> paramList = new ArrayList<>();
            paramList.add(new Param("username", "suvan"));
            paramList.add(new Param("username", "liushuwei0925@gmail.com"));
            paramList.add(new Param("email", "liushuwei0925@gmail.com"));

        UserDO user = new UserDO();
            user.setId(6);
            user.setName("suvan");
            user.setRank("admin");
            user.setState(1);

        Cookie autoLoginCookie = new Cookie(ParamConst.AUTHENTICATION, JwtTokenUtil.createToken(user));

        for (Param param : paramList) {
            System.out.println("input: key=" + param.key + ", value=" + param.value);

            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.get(API_ACCOUNT)
                            .param(param.key, (String) param.value)
                            .cookie(autoLoginCookie)
            ).andExpect(MockMvcResultMatchers.status().isOk())
             .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                    .andReturn();

            this.confirmMvcResultModelMapShouldHavaKeyItems(result,
                    "email", "sex", "birthday", "position", "description",
                    "createtime", "username", "avator", "state");
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】 test same time input two param get user information throw exception
     *      - database exception
     *          - no exist username, exist email
     */
    @Test
    public void testSameTimeInputTwoParamGetUserInformationThrowException() throws Exception {
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get(API_ACCOUNT)
                            .param("username", "failsuvan")
                            .param("email", "liushuwei0925@gmail.com")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""));
        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertThat(throwable, CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)));
            Assert.assertEquals(throwable.getMessage(), ApiMessage.NO_USER);
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】 test get user information throws exception
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    public void testGetUserInfoThrowsException() throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
            alist.add(new Param("username", null));
            alist.add(new Param("username", "a"));
            alist.add(new Param("username", "*asdf-="));
            alist.add(new Param("username", "12312311111111111111111"));
            alist.add(new Param("username", "suvanaa"));
            alist.add(new Param("emial", null));
            alist.add(new Param("email", "asdfasfsfaasf@"));
            alist.add(new Param("email", "suvan@liushuwei.cn"));

        for (Param param : alist) {
            System.out.println("params: " + param.key + " = " + param.value);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ACCOUNT).param(param.key, (String) param.value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class))
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/state】test get user activate state success
     */
    @Test
    public void testGetUserActivateStateSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(API_ACCOUNT_STATE).param("username", "suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/state】test get user activate state throw exception
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    public void testGetUserActivateStateThrowException() throws Exception {
        String key = "username";
        String[] values = {null, "a", "12**+-3123", "hello"};

        for (String value : values) {
            System.out.println("input param: " + key + " = " + value);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ACCOUNT_STATE).param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class))
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/login】 test login success
     */
    @Test
    public void testLoginSuccess() throws Exception {
        //设置 ServletContext，统计登录人数
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.LOGIN_USER, 0);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        //输入参数
        String inputUsername = "suvan";
        String inputPassword = "123456";

        String reuqesetBody = "{\"username\":\"" + inputUsername + "\", \"password\":\"" + inputPassword + "\"}";
        System.out.println("input reqeust-body: " + reuqesetBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(API_ACCOUNT_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reuqesetBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.cookie().exists(ParamConst.AUTHENTICATION))
         .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
            .andReturn();

        this.confirmMvcResultModelMapShouldHavaKeyItems(result, "state", "authentication");

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/login】 test login throw Exception
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    public void testLoginThrowException() throws Exception {
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.LOGIN_USER, 0);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        String[][] params = {{null, null}
                , {null, "123456"}, {"s", "123456"}, {"suvan*-=", "123456"}, {"hello", "123456"}
                , {"test@gmail.com", "123456"}, {"liushuwei0925@gmail.com", "asdfasf"}
                , {"suvan", null}, {"suvan", "123"}, {"suvan", "asdfasfa"}};

        for (String[] param: params) {
            String username = param[0];
            String password = param[1];
            String requestBody = "{\"username\":\"" + username + "\"," + "\"password\":\"" + password + "\"}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post(API_ACCOUNT_LOGIN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(TokenErrorException.class))
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/logout】 test logout success
     *      - set session
     *      - add already login user cookie
     */
    @Test
    public void testLogoutSuccess() throws Exception {
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.LOGIN_USER, 0);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        UserDO user = new UserDO();
            user.setId(5);
            user.setName("suvan");
            user.setRank("admin");
            user.setState(1);
        String authentication = JwtTokenUtil.createToken(user);

        mockMvc.perform(
                MockMvcRequestBuilders.get(API_ACCOUNT_LOGOUT)
                        .cookie(new Cookie(ParamConst.AUTHENTICATION, authentication))
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/logout】 test logout throw Exception
     *      - no login, test @LogintAuthorization
     */
    @Test
    public void testLogoutThrowException() throws Exception {
        try {
            //no Cookie, do ApiInterceptor do interceptor
            mockMvc.perform(
                    MockMvcRequestBuilders.get(API_ACCOUNT_LOGOUT)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertTrue(throwable instanceof AccountErrorException);
            Assert.assertEquals(throwable.getMessage(), ApiMessage.NO_PERMISSION);
        }
    }

    /**
     * 【/api/account/register】test register user success
     */
    @Test
    @Transactional
    public void testRegisterUserSuccess() throws Exception {
        String username = "apiTestUser";
        String password = "apiTestPassword";
        String email = "apiTestEmail@neubBs.com";

        String requestBody = "{\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\","
                + "\"email\":\"" + email + "\"}";
        System.out.println("input request-body: " + requestBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(API_ACCOUNT_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        this.confirmMvcResultModelMapShouldHavaKeyItems(result,
                "username", "email", "sex", "birthday", "position", "description", "avator", "state", "createtime");

        //confirm forum_user_action
        int userId = userDAO.getUserByName(username).getId();
        Assert.assertNotNull(userActionDAO.getUserAction(userId));

        //becasue register will create personal directory on ftp server, so need to remove
        userId = userDAO.getMaxUserId();
        FtpUtil.deleteDirectory("/user/" + userId + "-" + username);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/register】test register user throw exception
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    @Transactional
    public void testRegisterUserThrowException() throws Exception {
        String[][] params = {
                {null, null, null},
                {null, "123456", "only@neubbs.com"}, {"only", null, "only@neubbs.com"}, {"only", "123456", null},
                {null, null, "onlu@neubbs.com"}, {null, "123456", null}, {"only", null, null},
                {"o", "123456", "only@neubbs.com"}, {"onlyw$*=", "123456", "only@neubbs.com"},
                {"only", "1", "only@neubbs.com"},
                {"only", "123456", "only@"},
                {"suvan", "123456", "only@neubbs.com"}, {"only", "123456", "liushuwei0925@gmail.com"}
        };

        for (String[] arr : params) {
            String requestBody = "{\"username\":\"" + arr[0] + "\","
                    + "\"password\":\"" + arr[1] + "\","
                    + "\"email\":\"" + arr[2] + "\"}";
            System.out.println("requestbody: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post(API_ACCOUNT_REGISTER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class),
                                CoreMatchers.instanceOf(DatabaseOperationFailException.class)
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/update-profile】 test user update personal profile success
     *      - @LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testUserUpdatePersonalProfileSuccess() throws Exception {
        Integer newSex = 1;
        String newBirthday = "1996-09-25";
        String newPosition = "Neusoft School";
        String newDescription = "hello neubbs";
        String requestBody = "{" + this.getJsonField("sex", newSex) + ", "
                + this.getJsonField("birthday", newBirthday) + ", "
                + this.getJsonField("position", newPosition) + ", "
                + this.getJsonField("description", newDescription) + "}";
        System.out.println("input request-body information: " + requestBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/update-profile")
                    .cookie(this.getAlreadLoginUserCookie())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andReturn();

        //judge $.model
        this.confirmMvcResultModelMapShouldHavaKeyItems(result,
                "email", "sex", "birthday", "position", "description",
                "avator", "state", "createtime", "username");

        //check database data
        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());
        Map modelMap = (Map) resultMap.get("model");
        UserDO updatedUser = userDAO.getUserByName((String) modelMap.get("username"));
        Assert.assertEquals(newSex, updatedUser.getSex());
        Assert.assertEquals(newBirthday, updatedUser.getBirthday());
        Assert.assertEquals(newPosition, updatedUser.getPosition());
        Assert.assertEquals(newDescription, updatedUser.getDescription());

        printSuccessPassTestMehtodMessage();
    }


    /**
     * 【/api/account/update-password】 test user update password success
     *      - new user already login and activated (pass @LoginAuthorization @AccountActivation)
     */
    @Test
    @Transactional
    public void testUserUpdatePasswordSuccess() throws Exception {
        String inputUsername = "suvan";
        String inputNewPassword = "liushuwei";
        String requestBody = "{\"username\":\"" + inputUsername + "\",\"password\":\"" + inputNewPassword + "\"}";
        System.out.println("input request-body:" + requestBody);

        String authentication = JwtTokenUtil.createToken(userService.getUserInfoByName(inputUsername));

        mockMvc.perform(
                MockMvcRequestBuilders.post(API_ACCOUNT_UPDATE_PASSWORD)
                        .cookie(new Cookie(ParamConst.AUTHENTICATION, authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //database check
        UserDO user = userService.getUserInfoByName(inputUsername);
        Assert.assertEquals(user.getName(), inputUsername);
        Assert.assertEquals(user.getPassword(), SecretUtil.encryptUserPassword(inputNewPassword));

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/update-password】 test user update password throw exception
     *      - no login
     *      - account no activated
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    @Transactional
    public void testUserUpdatePasswordThrowException() throws Exception {
        //no login
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post(API_ACCOUNT_UPDATE_PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success.").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_PERMISSION))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertThat(throwable, (CoreMatchers.instanceOf(AccountErrorException.class)));
            Assert.assertEquals(throwable.getMessage(), ApiMessage.NO_PERMISSION);
        }

        //account no activate
        UserDO noActivationUser = new UserDO();
            noActivationUser.setName("noActivationUser");
            noActivationUser.setState(0);

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post(API_ACCOUNT_UPDATE_PASSWORD)
                            .cookie(new Cookie(ParamConst.AUTHENTICATION, JwtTokenUtil.createToken(noActivationUser)))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_ACTIVATE))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertTrue(throwable instanceof AccountErrorException);
            Assert.assertEquals(throwable.getMessage(), ApiMessage.NO_ACTIVATE);
        }

        //reqeust param error, no norm and database exception
        String[][] params = {
                {null, null}, {null, "123456"}, {"suvan", null},
                {"s", "123456"}, {"suvan*--0", "123456"},
                {"suvan", "123"},
                {"cookieNoSame", "123456"},
                {"noExist", "123456"}
        };

        String authentication = JwtTokenUtil.createToken(userService.getUserInfoByName("suvan"));
        Cookie cookie = new Cookie(ParamConst.AUTHENTICATION, authentication);

        for (String[] param : params) {
            String username = param[0];
            String newPassword = param[1];
            String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + newPassword + "\"}";
            System.out.println("input request-body: " + requestBody);

            if ("noExist".equals(username)) {
                //test database no exist user
                UserDO noExistUser = new UserDO();
                    noExistUser.setName("noExist");
                    noExistUser.setState(1);

                cookie.setValue(JwtTokenUtil.createToken(noExistUser));
            }

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post(API_ACCOUNT_UPDATE_PASSWORD)
                                .cookie(cookie)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class),
                                CoreMatchers.instanceOf(DatabaseOperationFailException.class)
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/update-email】test user update email success
     *      - @LoginAuthorization
     */
    @Test
    @Transactional
    public void testUserUpdateEmailSuccess() throws Exception {
        String inputUsername = "suvan";
        String inputEmail = "newEmail@email.com";
        String reqeustBody = "{\"username\":\"" + inputUsername + "\",\"email\":\"" + inputEmail + "\"}";
        System.out.println("input reqeust-body: " + reqeustBody);

        String authentication = JwtTokenUtil.createToken(userService.getUserInfoByName(inputUsername));

        mockMvc.perform(
                MockMvcRequestBuilders.post(API_ACCOUNT_UPDATE_EMAIL)
                        .cookie(new Cookie(ParamConst.AUTHENTICATION, authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqeustBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //database data check
        UserDO user = userService.getUserInfoByName(inputUsername);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getEmail(), inputEmail);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/update-email】test user update email throw exception
     *      - no login
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    @Transactional
    public void testUserUpdateEmailThrowException() throws Exception {
        //no login
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post(API_ACCOUNT_UPDATE_EMAIL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof AccountErrorException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_PERMISSION);
        }

        //request param error, no norm and database exception
        String[][] params = {
                {null, null}, {null, "test@test.com"}, {"test", null},
                {"t", "test@test.com"}, {"test*~", "test@test.com"},
                {"test", "test@"},
                {"ahonn", "test@test.com"},
                {"suvan", "liushuwei0925@gmail.com"},
                {"noExist", "test@test.com"}
        };

        String authentication = JwtTokenUtil.createToken(userService.getUserInfoByName("suvan"));
        Cookie userCookie = new Cookie(ParamConst.AUTHENTICATION, authentication);

        for (String[] param: params) {
            String username = param[0];
            String newEmail = param[1];
            String requestBody = "{\"username\":\"" + username + "\",\"email\":\"" + newEmail + "\"}";

            if ("noExist".equals(username)) {
                //if cookie exist user, but database user, will throw exception
                UserDO noExistUser = new UserDO();
                    noExistUser.setName("noExist");

                userCookie.setValue(JwtTokenUtil.createToken(noExistUser));
            }

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post(API_ACCOUNT_UPDATE_EMAIL)
                            .cookie(userCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("model").exists());

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
     * 【/api/account/activate】 test send email to activate user success
     *      - 主线程结束，守护线程也会放弃执行 taskEecutor.execute() 激活的都是守护线程
     */
    @Test
    @Transactional
    public void testSendEmailToActivateUserSuccess() throws Exception {
        String email = "13202405189@163.com";
        String requestBody = "{\"email\":\"" + email + "\"}";
        System.out.println("input reqeust-body: " + requestBody);

        //register new user, no accept return value, after rollback
        userService.registerUser("activateUser", "123456", email);

        mockMvc.perform(
                MockMvcRequestBuilders.post(API_ACCOUNT_ACTIVATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

       /*
        * need to blocking main thread, otherwise no do taskExecutor send eamil
        * taskExecutor active thread count > 0, try blocking main thread 20s
        */
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) webApplicationContext.getBean("taskExecutor");
        int timer = 1;
        while (taskExecutor.getActiveCount() != 0 && timer < 20) {
            Thread.sleep(1000);
            System.out.println("already wait " + (timer++) + "s");
        }
        System.out.println("send eamil success !");

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/activate】test send eamil to activate user throw exception
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    @Transactional
    public void testSendEmailToActivateUserThrowException() throws Exception{
        //request param error, no norm
       String [] emailParams = {null, "test@","liushuwei0925@gmail.com"};

        for (String email: emailParams) {
            String requestBody = "{\"email\":\"" + email + "\"}";
            System.out.println("input post param request-body: " +requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post(API_ACCOUNT_ACTIVATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
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

       //new register user
        String email = "alreadySend@neubbs.com";
        userService.registerUser(email.substring(0, email.indexOf("@")), "123456", email);

        //get redis service on web container
        ICacheService redisService = (ICacheService) webApplicationContext.getBean("redisServiceImpl");
        //redisService.saveUserEmailKey(email, "activate", SetConst.EXPIRE_TIME_SIXTY_SECOND_MS);

        mockMvc.perform(
                MockMvcRequestBuilders.post(API_ACCOUNT_ACTIVATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"" + email + "\"}")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.WAIT_TIMER))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.timer").exists());

        System.out.println("send email timer limit effective!");

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/validate】 test validate token activate user success
     */
    @Test
    @Transactional
    public void testValidateTokenActivateUserSuccess() throws Exception {
       //register new user
        UserDO user = userService.registerUser("testValidate", "123456", "testValidate@neubbs.com");

        //build token
        String token = SecretUtil.encryptBase64(user.getEmail() + "-" + StringUtil.getTodayTwentyFourClockTimestamp());

        //validate token and activate user
        mockMvc.perform(
                MockMvcRequestBuilders.get(API_ACCOUNT_VALIDATE).param("token", token)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //confirm database user activated
        Assert.assertTrue(userService.getUserInfoByEmail(user.getEmail()).getState() == 1);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/validate】test validate token activate user throw exception
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    @Transactional
    public void testValidateTokenActivateUserThrowException() throws Exception {
        String [] parmas = {
                 null,
                "1234", "awsfasdf", "asdfasdf-qweqwe", "13412-ASDFAF-qqqq",
                "aasdfasf-" + StringUtil.getTodayTwentyFourClockTimestamp(),
                "test@neubs.com-" + System.currentTimeMillis(),
                "test@nuebbs.com-" + StringUtil.getTodayTwentyFourClockTimestamp(),
                "liushuwei0925@gmail.com-" + StringUtil.getTodayTwentyFourClockTimestamp(),
        };

        for (String param: parmas) {
            String token = param != null ? SecretUtil.encryptBase64(param) : null;
            System.out.println("input token=" + token);

            try {
               mockMvc.perform(
                       MockMvcRequestBuilders.get(API_ACCOUNT_VALIDATE).param("token", token)
               ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(TokenErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class)
                        )
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/captcha】 test generate picture captcha success
     *      - need judge session exist captcha text
     *      - nedd judge page display content type is image/jpeg
     */
    @Test
    public void testGerneatePictureCaptchaSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(API_ACCOUNT_CAPTCHA).accept(MediaType.IMAGE_JPEG_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.content().contentType("image/jpeg"))
         .andReturn();

        Assert.assertNotNull(result.getRequest().getSession().getAttribute(SetConst.SESSION_CAPTCHA));

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/check-captcha】 test check user input captcha match success
     *      - need session save key-value
     */
    @Test
    public void testCheckUserInputCaptchaMatchSuccess() throws Exception {
        //web container get captcha service
        ICaptchaService captchaService = (ICaptchaService) this.webApplicationContext.getBean("captchaServiceImpl");
        String captcha = captchaService.getCaptchaText();

        mockMvc.perform(
                MockMvcRequestBuilders.get(API_ACCOUNT_CHECK_CAPTCHA)
                    .sessionAttr(SetConst.SESSION_CAPTCHA, captcha)
                    .param("captcha", captcha)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/check-captcha】 test check user input captcha match throw exception
     *      - request param error, no norm
     *      - no to generate captcha picture, session no captcha text
     */
    @Test
    public void testCheckUserInputCaptchaMatchThrowException() throws Exception {
       String[] params = {null, "123", "abs", "ABC", "12asdfABN", "*0-=123", "55555"};

       for (String param: params) {
           System.out.println("input captcha=" + param);

           try {
               mockMvc.perform(
                       MockMvcRequestBuilders.get(API_ACCOUNT_CHECK_CAPTCHA).param("captcha", param)
               ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.CAPTCHA_INCORRECT))
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

       //test no to generate captcha picture, session no captcha text
       Producer captchaProducer = (Producer) webApplicationContext.getBean("captchaProducer");
       RequestBuilder request = servletContext -> {
           MockHttpServletRequest mockRequest = new MockHttpServletRequest();

           mockRequest.setMethod(HttpMethod.GET);
           mockRequest.setRequestURI(API_ACCOUNT_CHECK_CAPTCHA);
           mockRequest.getSession().setAttribute(SetConst.SESSION_CAPTCHA, captchaProducer.createText());
           mockRequest.setParameter("captcha", "ksA23");

           return mockRequest;
       };

       try {
           mockMvc.perform(request)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(ApiMessage.CAPTCHA_INCORRECT))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

       } catch (NestedServletException ne) {
           Assert.assertSame(ne.getRootCause().getClass(), AccountErrorException.class);
           Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.CAPTCHA_INCORRECT);
       }

       printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/forget-password】 test user forget password send email update new temp password success
     */
    @Test
    @Transactional
    public void testUserForgetPasswordSendEmailUpdateNewTempPasswordSuccess() throws Exception {
        String email = "liushuwei0925@gmail.com";
        String requestBody = "{\"email\":\"" + email + "\"}";
        System.out.println("input request-body:" + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post(API_ACCOUNT_FORGET_PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //web container get thread poor executor object
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) webApplicationContext.getBean("taskExecutor");
        int timer = 1;
        while (taskExecutor.getActiveCount() != 0 || timer < 20) {
            Thread.sleep(1000);
            System.out.println("already wait " + (timer++) + "s");
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/forget-password】 test user forget password send email update new temp password throw exception
     *      - request param error, no norm
     *      - database exception
     */
    @Test
    @Transactional
    public void testUserForgetPasswordSendEmailUpdateNewTempPasswordThrowException() throws Exception {
        String[] parmas = {null, "test@", "test@neubbs.com"};

        for (String param: parmas) {
            String requestBody = "{\"email\":\"" + param + "\"}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post(API_ACCOUNT_FORGET_PASSWORD)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
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
}
