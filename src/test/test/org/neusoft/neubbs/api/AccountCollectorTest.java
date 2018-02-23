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
import org.neusoft.neubbs.controller.filter.ApiFilter;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IUserActionDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.exception.PermissionException;
import org.neusoft.neubbs.exception.ServiceException;
import org.neusoft.neubbs.service.ICacheService;
import org.neusoft.neubbs.service.ICaptchaService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.FtpUtil;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import javax.ws.rs.HttpMethod;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Account api 测试
 *      - 测试 /api/account
 *      - 测试 /api/account/state
 *      - 测试 /api/account/login
 *      - 测速 /api/account/logout
 *      - 测试 /api/account/register
 *      - 测试 /api/account/update-profile
 *      - 测试 /api/account/update-password
 *      - 测试 /api/account/update-email
 *      - 测试 /api/account/activate
 *
 * 【注意】 设置配置文件(需设置 ApplicationContext.xml 和 mvc.xm,否则会报错)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"}),
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

    private final String API_ACCOUNT_VALIDATE = "/api/account/validate";
    private final String API_ACCOUNT_CHECK_CAPTCHA = "/api/account/check-captcha";
    private final String API_ACCOUNT_FORGET_PASSWORD = "/api/account/forget-password";

    static class Param {
        String key;
        Object value;

        Param (String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    /*
     * ***********************************************
     * init method
     * ***********************************************
     */

    @BeforeClass
    public static void init() {
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * api 测试执行前构建
     * - 方式1：依赖 Spring 上下文，需加入 @ContextConfiguration 类注解，加载配置文件
     * - 方式2：自主构建控制器，可加入拦截器，过滤器之类
     */
    @Before
    public void setup() {
        //method 1
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.LOGIN_USER, 0);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .addFilter(new ApiFilter())
                .build();

        //method 2, custom default environment
        //AccountController accountController = (AccountController) webApplicationContext.getBean("accountController");
        //StandaloneMockMvcBuilder standBuild = MockMvcBuilders.standaloneSetup(accountController);
        //    standBuild.addFilter(new ApiFilter());
        //    standBuild.addInterceptors(new ApiInterceptor());
        //this.mockMvc = standBuild.build();
    }

    /*
     * ***********************************************
     * api test method
     * ***********************************************
     */

    /**
     * 测试 /api/account
     *      - 获取用户信息成功
     */
    @Test
    public void testGetUserInfoSuccess() throws Exception {
        ArrayList<Param> paramList = new ArrayList<>();
            paramList.add(new Param("username", "suvan"));
            paramList.add(new Param("username", "liushuwei0925@gmail.com"));
            paramList.add(new Param("email", "liushuwei0925@gmail.com"));

        for (Param param : paramList) {
            System.out.println("input: key=" + param.key + ", value=" + param.value);

            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param(param.key, (String) param.value)
            ).andExpect(MockMvcResultMatchers.status().is(200))
             .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                    .andReturn();

            this.isExistKeyItems(result,
                    "email", "sex", "birthday", "position", "description",
                    "avator", "state", "createtime", "userid", "username",
                    "following", "followed", "like", "collect", "attention", "topic", "reply");
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account
     *      - [✔] 获取用户信息，常同时输入两个不同参数，其中之一出现异常
     */
    @Test
    public void testGetUserInfoSameInputTwoParamException() throws Exception {
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param("username", "noUser")
                            .param("email", "liushuwei0925@gmail.com")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertThat(throwable, CoreMatchers.instanceOf(ServiceException.class));
            Assert.assertEquals(throwable.getMessage(), ApiMessage.NO_USER);
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account
     *      - 获取用户信息异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testGetUserInfoException () throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
            alist.add(new Param("username", null));
            alist.add(new Param("username", "a"));
            alist.add(new Param("username", "*asdf-="));
            alist.add(new Param("username", "12312311111111111111111"));
            alist.add(new Param("username", "suvanaa"));
            alist.add(new Param("email", null));
            alist.add(new Param("email", "asdfasfsfaasf@"));
            alist.add(new Param("email", "suvan@liushuwei.cn"));

        for (Param param : alist) {
            System.out.println("params: " + param.key + " = " + param.value);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account")
                                .param(param.key, (String) param.value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(ServiceException.class))
                        )
                );
            }
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/state
     *      - 获取用户状态成功
     */
    @Test
    public void testGetActivateState() throws Exception {
        //already exist user
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/state")
                        .param("username", "suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/state
     *      - 获取用户激活状态异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testGetUserActivateStateException() throws Exception {
        String key = "username";
        String[] values = {null, "h", "123", "hello"};

        for (String value : values) {
            System.out.println("input param: " + key + " = " + value);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/state")
                                .param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/following
     *      - 获取用户所有主动关注人信息列表成功
     */
    @Test
    public void testListUserFollowingUserInfoSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/following")
                    .param("userid", "6")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/following
     *      - 获取用户所有主动关注人信息列表异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testListUserFollowingUserInfoException() throws Exception {
        String key = "userid";
        String[] values = {null, "abc", "123*", "100000000000", "88888"};

        for (String value: values) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/following")
                            .param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/followed
     *      - 获取用户所有被关注信息列表成功
     *      - 列表字段
     *          - email, sex, birthday, position, description,
     *          - avator, state, create, userid, username
     */
    @Test
    public void testListUserFollowedUserInfoSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/followed")
                    .param("userid", "6")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/followed
     *      - 获取所有用户所有被关注人信息列表异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testListUserFollowedUserInfoException() throws Exception {
        String key = "userid";
        String[] values = {null, "abc", "123*", "100000000000", "88888"};

        for (String value: values) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/followed")
                            .param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));
            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/login
     *      - 账户登陆成功
     *      - 列表字段
     *          - email, sex, birthday, position, description,
     *          - avator, state, create, userid, username
     */
    @Test
    public void testLoginAccountSuccess() throws Exception {
        //build request-body
        String username = "suvan";
        String password = "123456";
        String requestBody = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        System.out.println("input request-body: " + requestBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.cookie().exists(ParamConst.AUTHENTICATION))
         .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
            .andReturn();

        this.isExistKeyItems(result, "state", "authentication");
        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/login
     *      - 账户登陆异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    public void testLoginAccountException() throws Exception {
        String[][] params = {
                {null, null}, {"suvan", null}, {null, "123456"},
                {"s", "123456"}, {"suvan*-=", "123456"}, {"noUser", "123456"},
                {"test@gmail.com", "123456"}, {"liushuwei0925@gmail.com", "xxxxxxx"},
                {"suvan", "123"}, {"suvan", "xxxxx"}};

        for (String[] param : params) {
            //build request-body
            String username = param[0];
            String password = param[1];
            String requestBody = "{\"username\":\"" + username + "\"," + "\"password\":\"" + password + "\"}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(ServiceException.class))
                        )
                );
            }
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/logout
     *      - 注销账户成功
     *      - 需要权限：@LoginAuthorization
     */
    @Test
    public void testLogoutAccountSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/logout")
                        .cookie(this.getAlreadyLoginUserCookie())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/logout
     *      - 注销账户异常
     *          - [✔] no login
     */
    @Test
    public void testLogoutThrowException() throws Exception {
        try {
            //no Cookie, to do ApiInterceptor validate
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account/logout")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof PermissionException);
            Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.NO_PERMISSION);
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/register
     *      - 注册账户成功
     */
    @Test
    @Transactional
    public void testRegisterAccountSuccess() throws Exception {
        //build params -> request-body
        String username = "apiTestUser";
        String password = "apiTestPassword";
        String email = "apiTestEmail@neubBs.com";
        String requestBody = "{\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\","
                + "\"email\":\"" + email + "\"}";
        System.out.println("input request-body: " + requestBody);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
            .andReturn();

        this.isExistKeyItems(result,
                "email", "sex", "birthday", "position", "description",
                "avator", "state", "createtime", "userid", "username"
        );

        //validate 'forum_user_action'
        UserDO user = userDAO.getUserByName(username);
        Assert.assertNotNull(userActionDAO.getUserAction(user.getId()));

        //so need to remove user personal directory in ftp server, because register account automatic create
        FtpUtil.deleteDirectory("/user/" + user.getId() + "-" + username);

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/register
     *      - 注册账户异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    @Transactional
    public void testRegisterAccountException() throws Exception {
        String[][] params = {
                {null, null, null},
                {null, "123456", "only@neubbs.com"}, {"only", null, "only@neubbs.com"}, {"only", "123456", null},
                {null, null, "onlu@neubbs.com"}, {null, "123456", null}, {"only", null, null},
                {"o", "123456", "only@neubbs.com"}, {"only$*=", "123456", "only@neubbs.com"},
                {"only", "1", "only@neubbs.com"},
                {"only", "123456", "only@"},
                {"suvan", "123456", "only@neubbs.com"}, {"only", "123456", "liushuwei0925@gmail.com"}
        };

        for (String[] arr : params) {
            String requestBody = "{\"username\":\"" + arr[0] + "\","
                    + "\"password\":\"" + arr[1] + "\","
                    + "\"email\":\"" + arr[2] + "\"}";
            System.out.println("request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        this.printSuccessMessage();
    }


    /**
     * 测试 /api/account/update-profile
     *      - 修改用户基本信息成功
     *      - 需要权限：@LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testUpdateUserInfoSuccess() throws Exception {
        //build request-body
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
                        .cookie(this.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.sex").value(newSex))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.birthday").value(newBirthday))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.position").value(newPosition))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.description").value(newDescription))
                .andReturn();

        this.isExistKeyItems(result,
                "email", "sex", "birthday", "position", "description",
                "avator", "state", "createtime", "userid", "username");

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/update-profile
     *      - 修改用户基本信息异常
     *          - [✔] no permission（@LoginAuthorization, @AccountActivation）
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     */
    @Test
    @Transactional
    public void testUpdateUserInfoException() throws Exception {
        String apiUrl = "/api/account/update-profile";

        //no login
        this.testApiThrowNoPermissionException(apiUrl, RequestMethod.POST, null);

        //account no activated
        this.testApiThrowNoPermissionException(apiUrl, RequestMethod.POST, this.getNoActivatedUserDO());

        //request
        String[][] params = {
                {null, null, null, null}, {null, "1996-09-25", "深圳", "描述"},
                {"12", "1996-09-25", "深圳", "描述"}, {"1", "1996-02-25 错误的时间", "深圳", "描述"}
        };

        for (String[] param: params) {
            Integer newSex = param[0] == null ? null : Integer.parseInt(param[0]);
            String newBirthday = param[1];
            String newPosition = param[2];
            String newDescription = param[3];
            String requestBody = "{" + this.getJsonField("sex", newSex) + ", "
                    + this.getJsonField("birthday", newBirthday) + ", "
                    + this.getJsonField("position", newPosition) + ", "
                    + this.getJsonField("description", newDescription) + "}";
            System.out.println("input request-body information: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/update-profile")
                                .cookie(this.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CoreMatchers.notNullValue()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Throwable throwable = ne.getRootCause();
                Assert.assertThat(throwable,
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }
    }


    /**
     * 测试 /api/account/update-password
     *      - 账户修改密码成功
     *      - 需要权限：@LoginAuthorization @AccountActivation
     */
    @Test
    @Transactional
    public void testUpdateUserPassword() throws Exception {
        String username = "suvan";
        String newPassword = "new123456";
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + newPassword + "\"}";
        System.out.println("input request-body:" + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/update-password")
                        .cookie(this.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //again validate database user
        UserDO user = userService.getUserInfoByName(username);
        String secretPassword = SecretUtil.encryptMd5(SecretUtil.encryptMd5(newPassword) + newPassword);
        Assert.assertEquals(secretPassword, user.getPassword());

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/update-password
     *      - 修改账户密码异常
     *          - [✔] no permission (@LoginAuthorization, @AccountActivation)
     *              - no login
     *              - account no activated
     *              - input user not match cookie user
     *          - [✔] request param error, no norm
     */
    @Test
    @Transactional
    public void testUserUpdatePasswordThrowException() throws Exception {
        // no login
        this.testApiThrowNoPermissionException("/api/account/update-password", RequestMethod.POST, null);

        //account no activated
        this.testApiThrowNoPermissionException("/api/account/update-password", RequestMethod.POST, this.getNoActivatedUserDO());

        //input username no match cookie user
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/account/update-password")
                        .cookie(this.getOtherAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"suvan\",\"password\":\"123456\"}")
                        .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_PERMISSION))
             .andExpect(MockMvcResultMatchers.jsonPath("$.mode").value(CoreMatchers.notNullValue()));

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof PermissionException);
            Assert.assertEquals(ApiMessage.NO_PERMISSION, ne.getRootCause().getMessage());
        }

        //request params error, no norm
        String[][] params = {
                {null, null}, {null, "123456"}, {"suvan", null},
                {"s", "123456"}, {"suvan*--0", "123456"},
                {"suvan", "123"}, {"suvan", "k"}
        };

        for (String[] param : params) {
            String username = param[0];
            String newPassword = param[1];
            String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + newPassword + "\"}";
            System.out.println("input request-body: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/update-password")
                                .cookie(this.getAlreadyLoginUserCookie())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertTrue(ne.getRootCause() instanceof ParamsErrorException);
                Assert.assertEquals(ApiMessage.PARAM_ERROR, ne.getRootCause().getMessage());
            }
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/update-email
     *      - 测试修改邮箱成功
     *      - 需要权限：@LoginAuthorization
     */
    @Test
    @Transactional
    public void testUpdateUserEmailSuccess() throws Exception {
        String username = "suvan";
        String newEmail = "newEmail@email.com";
        String requestBody = "{\"username\":\"" + username + "\",\"email\":\"" + newEmail + "\"}";
        System.out.println("input request-body: " + requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/update-email")
                        .cookie(this.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //again validate database user
        UserDO user = userService.getUserInfoByName(username);
        Assert.assertNotNull(user);
        Assert.assertEquals(newEmail, user.getEmail());

        this.printSuccessMessage();
    }

    /**
     *  测试 /api/account/update-email
     *      - 修改用户邮箱异常
     *          - [✔] no permission
     *              - no login
     *              - input user not match cookie user
     *          - [✔] request param error, no norm
     */
    @Test
    @Transactional
    public void testUpdateUserEmailException() throws Exception {
        //no login
        this.testApiThrowNoPermissionException("/api/account/update-email", RequestMethod.POST, null);

        //request param error, no norm
        String[][] params = {
                {null, null}, {null, "test@test.com"}, {"test", null},
                {"t", "test@test.com"}, {"test*~", "test@test.com"}, {"test", "test@"}
        };

        for (String[] param: params) {
            String username = param[0];
            String newEmail = param[1];
            String requestBody = "{\"username\":\"" + username + "\",\"email\":\"" + newEmail + "\"}";

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/update-email")
                            .cookie(this.getAlreadyLoginUserCookie())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("message").value(ApiMessage.PARAM_ERROR))
                 .andExpect(MockMvcResultMatchers.jsonPath("model").value(CoreMatchers.notNullValue()));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(), CoreMatchers.instanceOf(ParamsErrorException.class));
                Assert.assertEquals(ApiMessage.PARAM_ERROR, ne.getRootCause().getMessage());
            }
        }

        //input user not match cookie user
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/account/update-email")
                        .cookie(this.getAlreadyLoginUserCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"otherUser\",\"email\":\"newUser@neubbs.com\"}")
                        .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_PERMISSION))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof PermissionException);
            Assert.assertEquals(ApiMessage.NO_PERMISSION, ne.getRootCause().getMessage());
        }

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/activate
     *      - 测试激活账户成功
     *          - 主线程结束，守护线程也会放弃执行
     *              - 所以需要阻塞主线程，当发送邮件线程执行完毕后，再继续执行
     *              - taskExecutor.execute() 激活的都是守护线程
     */
    @Test
    @Transactional
    public void testActivateAccountSuccess() throws Exception {
        String email = "suvan-liu@qq.com";
        String requestBody = "{\"email\":\"" + email + "\"}";
        System.out.println("input request-body: " + requestBody);

        //register new user, to test
        userService.registerUser("testUser", "123456", email);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/activate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

       /*
        * need to blocking main thread, otherwise not send mail
        *   - loop to cyclic, active thread count > 0,
        *   - most to block main thread 60s
        */
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) webApplicationContext.getBean("taskExecutor");
        int timer = 1;
        while (taskExecutor.getActiveCount() > 0 && timer < 60) {
            Thread.sleep(1000);
            System.out.println("already wait " + (timer++) + "s");
        }
        System.out.println("send email '" + email + "'success !");

        this.printSuccessMessage();
    }

    /**
     * 测试 /api/account/activate
     *      - 测试激活账户异常
     *          - [✔] request param error, no norm
     *          - [✔] service exception
     *          - [✔] 60s send mail interval time for the same account
     */
    @Test
    @Transactional
    public void testActivateAccountException() throws Exception{
        //request param error, no norm
       String [] params = {null, "123", "test@", "liushuwei0925@gmail.com"};

        for (String email: params) {
            String requestBody = "{\"email\":\"" + email + "\"}";
            System.out.println("input post param request-body: " +requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/activate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(
                                CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
       }

       //test again send mail, exist interval time（not sent repeatedly）
       String email = "test@test.com";
       userService.registerUser(email.substring(0, email.indexOf("@")), "123456", email);

       //cache server set 60s send mail interval time for the same account
       ICacheService redisService = (ICacheService) webApplicationContext.getBean("redisServiceImpl");
       redisService.saveUserEmailKey(email);

       mockMvc.perform(
               MockMvcRequestBuilders.post("/api/account/activate")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"email\":\"" + email + "\"}")
                       .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.WAIT_TIMER))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.model.timer").value(CoreMatchers.notNullValue()));

        System.out.println("send email exist interval time limit , test success!");

        this.printSuccessMessage();
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
        String token = SecretUtil.encodeBase64(user.getEmail() + "-" + StringUtil.getTodayTwentyFourClockTimestamp());

        //validate token and activate user
        mockMvc.perform(
                MockMvcRequestBuilders.get(API_ACCOUNT_VALIDATE).param("token", token)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
         .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

        //confirm database user activated
        Assert.assertTrue(userService.getUserInfoByEmail(user.getEmail()).getState() == 1);

        this.printSuccessMessage();
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
            String token = param != null ? SecretUtil.encodeBase64(param) : null;
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
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        this.printSuccessMessage();
    }

    /**
     * 【/api/account/captcha】 test generate picture captcha success
     *      - need judge session exist captcha text
     *      - nedd judge page display content type is image/jpeg
     */
    @Test
    public void testGerneatePictureCaptchaSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/captch").accept(MediaType.IMAGE_JPEG_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.content().contentType("image/jpeg"))
         .andReturn();

        Assert.assertNotNull(result.getRequest().getSession().getAttribute(SetConst.SESSION_CAPTCHA));

        this.printSuccessMessage();
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

        this.printSuccessMessage();
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
                               CoreMatchers.instanceOf(ServiceException.class)
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
           Assert.assertSame(ne.getRootCause().getClass(), ServiceException.class);
           Assert.assertEquals(ne.getRootCause().getMessage(), ApiMessage.CAPTCHA_INCORRECT);
       }

       this.printSuccessMessage();
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

        this.printSuccessMessage();
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
                                CoreMatchers.instanceOf(ServiceException.class)
                        )
                );
            }
        }

        this.printSuccessMessage();
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 打印成功通过 Test 函数消息
     */
    public void printSuccessMessage() {
        String currentExecuteMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println("***************** 【" + currentExecuteMethod + "()】 " + " pass the test ****************");
    }

    /**
     * 获取已经登陆用户 Cookie
     *      - 设置 Suvan 账户
     *
     * @return Cookie 已经登录用户Cookie
     */
    public Cookie getAlreadyLoginUserCookie() {
        UserDO user = new UserDO();
            user.setId(6);
            user.setName("suvan");
            user.setRank("admin");
            user.setState(SetConst.ACCOUNT_ACTIVATED_STATE);

        return new Cookie(ParamConst.AUTHENTICATION, SecretUtil.generateUserInfoToken(user));
    }

    /**
     * 获取未激活的用户对象
     *
     * @return UserDO 未激活的用户对象
     */
    public UserDO getNoActivatedUserDO() {
        UserDO user = new UserDO();
            user.setId(5);
            user.setName("suvan");
            user.setRank("user");
            user.setState(SetConst.ACCOUNT_NO_ACTIVATED_STATE);

        return user;
    }

    /**
     * 获取其他已登陆用户的 Cookie
     *      - 默认是 suvan 用户，则该函数获取获取另外一个用户对象
     *
     * @return Cookie 其他已登陆用户的Cookie
     */
    public Cookie getOtherAlreadyLoginUserCookie() {
        UserDO otherUser = new UserDO();
            otherUser.setId(123);
            otherUser.setName("noMatchUser");
            otherUser.setRank("user");
            otherUser.setState(SetConst.ACCOUNT_ACTIVATED_STATE);

        return new Cookie(ParamConst.AUTHENTICATION, SecretUtil.generateUserInfoToken(otherUser));
    }

    /**
     * 生成 JSON 字段
     *      - 传入单个 key-value，生成 JSON 格式字符串
     *      - 字符串对象需加 ""， 其他 Object 对象不用
     *
     * @param key 键
     * @param value 值
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
     * 检查存在 Key 选项
     *      - 检测 MvcResult 的 mode 字段，存在指定 Key items
     *
     * @param result MvcResult 结果集
     * @param keyItems 多个 Key Item（可变参数）
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private void isExistKeyItems(MvcResult result, String... keyItems) throws UnsupportedEncodingException {
        Map resultMap = (Map) JSON.parse(result.getResponse().getContentAsString());
        Map resultModelMap = (Map) resultMap.get("model");

        Assert.assertEquals(keyItems.length, resultModelMap.size());
        Assert.assertThat((Set<String>) resultModelMap.keySet(), CoreMatchers.hasItems(keyItems));
    }

    /**
     * 访问 api，抛出用户无权限异常
     *
     * @param apiUrl api地址
     * @param requestMethod http请求方式
     * @param user 用户对象（用于构建Cookie）
     */
    private void testApiThrowNoPermissionException(String apiUrl, RequestMethod requestMethod, UserDO user) {
        //set post | get
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get(apiUrl);
        if (RequestMethod.POST.equals(requestMethod)) {
            mockRequest = MockMvcRequestBuilders.post(apiUrl);
        }

        //no login
        if (user != null) {
            mockRequest.cookie(new Cookie(ParamConst.AUTHENTICATION, SecretUtil.generateUserInfoToken(user)));
        }

        try {
            mockMvc.perform(
                    mockRequest
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.NO_PERMISSION))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));
        } catch (NestedServletException ne) {
            Assert.assertTrue(ne.getRootCause() instanceof PermissionException);

            //account no activated
            if (user != null && user.getState() == SetConst.ACCOUNT_NO_ACTIVATED_STATE) {
                Assert.assertEquals(ApiMessage.NO_ACTIVATE, ne.getRootCause().getMessage());
            } else {
                Assert.assertEquals(ApiMessage.NO_PERMISSION, ne.getRootCause().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
           throw new RuntimeException("no throw expected exception");
        }
    }
}
