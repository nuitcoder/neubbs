package test.org.neusoft.neubbs.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Login Api 测试 ，模拟 http 测试  Collector 内接口
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class LoginCollectorTestCase {
    @Mock
    private MockMvc mockMvc;

    @Before
    public void setup(){
        //this.mockMvc = MockMvcBuilders.standaloneSetup(new AccountController()).build();
    }

    @Test
    public void testLogin() throws Exception{
         //mockMvc.perform(get("/api/account")
         //                   .contentType(MediaType.APPLICATION_JSON)
         //                   .param("username","oneuser")
         //              ).andDo(print());
    }
}
