package test.org.neusoft.neubbs.api;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.api.AccountController;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TokenErrorException;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;

/**
 * Account api 测试
 *      - 声明 Spring
 *      - 开启 web 服务
 *      - 设置配置文件(需设置 applicatonContext.xml 和 mvc.xm,否则会报错)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class AccountCollectorTestCase {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private AccountController accountController;

    static class Param {
        String key;
        Object value;

        Param (String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 打印成功通过 Test 函数消息
     */
    public void printSuccessPassTestMehtodMessage () {
        //当前正在执行函数（调用此函数的函数）
        String currentDoingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        System.out.println("*************************** 【"+ currentDoingMethod + "()】 "
                + "pass all the tests ****************************");
    }

    @BeforeClass
    public static void init() {
//      //使用本地数据库（默认是云数据库）
//        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
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

        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 【/api/account】test no login get user information success
     */
    @Test
    public void testNoLoginGetUserInfoSuccess() throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
        alist.add(new Param("username", "suvan"));
        alist.add(new Param("username", "liushuwei0925@gmail.com"));
        alist.add(new Param("email", "liushuwei0925@gmail.com"));

        for (Param param: alist) {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param(param.key, (String) param.value)
            ).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】test already login get user information success
     */
    @Test
    public void testAreadyLoginGetUserInfoSuccess() throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
        alist.add(new Param("username", "suvan"));
        alist.add(new Param("username", "liushuwei0925@gmail.com"));
        alist.add(new Param("email", "liushuwei0925@gmail.com"));

        UserDO user = new UserDO();
        user.setId(6);
        user.setName("suvan");
        user.setRank("admin");
        user.setState(1);
        Cookie autoLoginCookie = new Cookie(ParamConst.AUTHENTICATION, JwtTokenUtil.createToken(user));

        for (Param param : alist) {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param(param.key, (String) param.value)
                            .cookie(autoLoginCookie)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】 test same time input two param get user information throw exception
     */
    @Test
    public void testSameTimeInputTwoParamGetUserInformation() throws Exception{
//        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
//        for (StackTraceElement stack: stacks) {
//            System.out.println(stack.getMethodName() + "***" + stack.getClassName());
//        }
        //测试参数优先级（username=错误参数[有效用户]，email=正确参数[无效用户]）
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param("username", "failsuvan")
                            .param("email", "liushuwei0925@gmail.com")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));

        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertThat(throwable, CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)));
            Assert.assertEquals(throwable.getMessage(), ApiMessage.NO_USER);
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】 test get user information throws exception
     */
    @Test
    public void testGetUserInfoThrowsException () throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
        alist.add(new Param("username", null));
        alist.add(new Param("username", "a"));
        alist.add(new Param("username", "*asdf-="));
        alist.add(new Param("username", "12312311111111111111111"));
        alist.add(new Param("username", "suvanaa"));
        alist.add(new Param("emial", null));
        alist.add(new Param("email", "asdfasfsfaasf@"));
        alist.add(new Param("email", "suvan@liushuwei.cn"));

        for (Param param: alist) {
            System.out.println("params: " + param.key + " = " + param.value);

            try{
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account")
                                .param(param.key, (String) param.value)
                ).andDo(MockMvcResultHandlers.print());
            } catch (NestedServletException ne) {
                //自定义异常通过
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)))
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     *【/api/account/state】test get user activate state success
     */
    @Test
    public void testGetUserActivateStateSuccess() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/state")
                        .param("username","suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/state】test get user activate state throw exception
     */
    @Test
    public void testGetUserActivateStateThrowException() throws Exception {
        String key = "username";
        String[] values = {null, "a", "12**+-3123", "hello"};

        for (String value: values) {
            System.out.println("params: " + key + " = " + value);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/state")
                                .param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)))
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
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.COUNT_LOGIN_USER, 0);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        //输入参数
        String inputUsername = "suvan";
        String inputPassword = "123456";

        String reuqesetBody = "{\"username\":\"" + inputUsername + "\", \"password\":\"" + inputPassword + "\"}";
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reuqesetBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model.state").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model.authentication").exists())
                .andExpect(MockMvcResultMatchers.cookie().exists(ParamConst.AUTHENTICATION))
                .andReturn();

        //结果比较
        Map<String, Object> resultMap = JsonUtil.toMapByJSONString(result.getResponse().getContentAsString());
        Map<String, Object> model = (Map<String, Object>)resultMap.get("model");

        Assert.assertTrue(model.size() == 2);
        String tokenAuthentication = (String) model.get(ParamConst.AUTHENTICATION);
        UserDO resultUser = JwtTokenUtil.verifyToken(tokenAuthentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
        Assert.assertNotNull(resultUser);
        Assert.assertEquals(resultUser.getName(), inputUsername);

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/login】 test login throw Exception
     */
    @Test
    public void testLoginThrowException() throws Exception{
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.COUNT_LOGIN_USER, 0);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        String[][] params = {{null,null}
                , {null, "123456"}, {"s", "123456"}, {"suvan*-=", "123456"}, {"hello", "123456"}
                , {"test@gmail.com", "123456"}, {"liushuwei0925@gmail.com", "asdfasf"}
                , {"suvan", null}, {"suvan", "123"}, {"suvan", "asdfasfa"}};

        for (int i = 0, len = params.length; i < len; i++) {
            String username = params[i][0];
            String password = params[i][1];
            String requestBody = "{\"username\":\"" + username +"\","
                    + "\"password\":\"" + password + "\"}";

            System.out.println("requestbody: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)),
                                CoreMatchers.is(CoreMatchers.instanceOf(TokenErrorException.class)))
                );

            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/logout】 test logout success
     */
    @Test
    public void testLogoutSuccess() throws Exception{
        this.webApplicationContext.getServletContext().setAttribute(ParamConst.COUNT_LOGIN_USER, 0);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        UserDO user = new UserDO();
        user.setId(5);
        user.setName("suvan");
        user.setRank("admin");
        user.setState(1);
        String authentication = JwtTokenUtil.createToken(user);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/logout")
                        .cookie(new Cookie(ParamConst.AUTHENTICATION, authentication))
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/logout】 test logout throw Exception
     *      - test @LogintAuthorization （pass ApiInterceptor）
     */
    @Test
    public void testLogoutThrowException() throws Exception {
        try {
            //no Cookie, ApiInterceptor do interceptor
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account/logout")
            );
        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertTrue(throwable instanceof AccountErrorException);
            Assert.assertEquals(throwable.getMessage(), ApiMessage.NO_PERMISSION);
        }
    }

    /**
     * 【/api/account/register】test register user success
     *      - @Transactional 执行完回滚
     */
    @Test
    @Transactional
    public void testRegisterUserSuccess() throws Exception{
        String username = "apiTestUser";
        String password = "apiTestPassword";
        String email = "apiTestEmail@neubs.com";

        String requestBody = "{\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\","
                + "\"email\":\"" + email + "\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model.username").value(username));

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/register】test register user throw exception
     *      - @Transactional
     */
    @Test
    @Transactional
    public void testRegisterUserThrowException() throws Exception {
        String[][] params = {
                {null,null,null},
                {null, "123456", "only@neubbs.com"}, {"only", null, "only@neubbs.com"}, {"only", "123456", null},
                {null, null, "onlu@neubbs.com"}, {null, "123456", null}, {"only", null, null},
                {"o", "123456", "only@neubbs.com"}, {"onlyw$*=", "123456", "only@neubbs.com"},
                {"only", "1", "only@neubbs.com"},
                {"only", "123456", "only@"},
                {"suvan", "123456", "only@neubbs.com"}, {"only", "123456", "liushuwei0925@gmail.com"}
        };

        for(String[] arr: params) {
            String username = arr[0];
            String password = arr[1];
            String email = arr[2];

            String requestBody = "{\"username\":\"" + username + "\","
                    + "\"password\":\"" + password + "\","
                    + "\"email\":\"" + email + "\"}";
            System.out.println("requestbody: " + requestBody);

            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/account/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.model").doesNotExist());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.instanceOf(ParamsErrorException.class),
                                CoreMatchers.instanceOf(AccountErrorException.class),
                                CoreMatchers.instanceOf(DatabaseOperationFailException.class))
                );
            }
        }

        printSuccessPassTestMehtodMessage();
    }
}
