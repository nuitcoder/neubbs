package test.org.neusoft.neubbs.api;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.filter.ApiFilter;
import org.neusoft.neubbs.controller.handler.DynamicSwitchDataSourceHandler;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.transaction.Transactional;
import java.util.List;

/**
 * File api 测试
 *      - 测试 /api/file/avatar
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class FileControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ApiTestUtil util;

    @Autowired
    private IUserService userService;

    /*
     * ***********************************************
     * init method
     * ***********************************************
     */

    @BeforeClass
    public static void init() {
        DynamicSwitchDataSourceHandler.setDataSource(SetConst.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .addFilter(new ApiFilter())
                .build();

        this.util = ApiTestUtil.getInstance(this.mockMvc);
    }

    /*
     * ***********************************************
     * api test method
     * ***********************************************
     */

    /**
     * 测试 /api/file/avatar
     *      - 测试上传用户头像成功
     *      - 需要权限：@LoginAuthorization, @AccountActivation
     */
    @Test
    @Transactional
    public void testUploadUserAvatarSuccess() throws Exception {
        //build MultipartFile type(html <> name, file name，file type，size)
        String htmlInputName = "avatarImageFile";
        String uploadFileName = "testAvatarFile";
        byte [] fileBytes = new byte[1024 * 1];
        MockMultipartFile[] files = {
                new MockMultipartFile(htmlInputName, uploadFileName + ".jpg", "image/jpg", fileBytes),
                new MockMultipartFile(htmlInputName, uploadFileName + ".png", "image/png", fileBytes),
                new MockMultipartFile(htmlInputName, uploadFileName + ".jpeg", "image/jpeg", fileBytes),
        };

        //upload user avatar image file
        for (MockMultipartFile file: files) {
            mockMvc.perform(
                    MockMvcRequestBuilders.fileUpload("/api/file/avatar")
                            .file(file)
                            .cookie(util.getAlreadyLoginUserCookie())
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiMessage.UPLOAD_SUCCESS))
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(CoreMatchers.notNullValue()));

           System.out.println("upload " + file.getOriginalFilename() + " success!");
        }

        /*
         * delete already upload user avatar image file
         *      - test local
         *          - upload path: user.image.upload.path = ... (neubbs.properties)
         *      - test ftp server
         */

        //System.out.println("delete local has bean uploaded test avatar file ......");
        //String localUploadPath = webApplicationContext.getServletContext()
        //        .getRealPath(neubbsConfig.getUserImageUploadPath());
        //File directory = new File(localUploadPath);
        //File[] allFiles = directory.listFiles();
        //for (File file: allFiles) {
        //    if (file.getName().contains(uploadFileName)) {
        //        file.deleteOnExit();
        //        System.out.println("already delete " + file.getName());
        //    }
        //}
        //System.out.println("all local test file delete finished!");

        System.out.println("delete ftp server has bean uploaded test avatar file ......");
        UserDO user = userService.getUserInfoByName("suvan");
        String ftpUserAvatarPath = "/user/" + user.getId() + "-" + user.getName() + "/avator/";

        List<String> listFileName = FtpUtil.listServerPathFileName(ftpUserAvatarPath);
        for (String fName: listFileName) {
            if (fName.contains(uploadFileName)) {
                FtpUtil.delete(ftpUserAvatarPath, fName);
                System.out.println("already delete " + fName);
            }
        }
        System.out.println("all ftp server test file delete finished!");

        util.printSuccessMessage();
    }

    /**
     * 测试 /api/file/avatar
     *      - 测试上传用户头像异常
     *          - [✔] no permission
     *              - no login
     *              - the account not activated
     *          - [✔] upload file format incorrect (no norm)
     */
    @Test
    @Transactional
    public void testUploadUserAvatarException() throws Exception {
        //no login
        util.testApiThrowNoPermissionException("/api/file/avatar", RequestMethod.POST, null);

        //the account not activated
        util.testApiThrowNoPermissionException("/api/file/avatar", RequestMethod.POST, util.getNoActivatedUserDO());

        //upload file format incorrect
        String htmlInputName = "avatarImageFile";
        String fileName = "testAvatarFile.jpg";
        String fileType = "image/jpg";
        byte[] fileBytes = new byte[1024 * 1024 * 1];
        MockMultipartFile[] files = {
                new MockMultipartFile(htmlInputName, fileName, fileType, new byte[0]),
                new MockMultipartFile(htmlInputName, "exceptionImage.ps", "image/ps" , fileBytes),
                new MockMultipartFile(htmlInputName, "exceptionImage.jpg", "jpg" , fileBytes),
                new MockMultipartFile(htmlInputName, "exceptionImage.avi", "avi" , fileBytes),
                new MockMultipartFile(htmlInputName, fileName, fileType ,new byte[1024 * 1024 * 6])
        };

        for (MockMultipartFile file: files) {
            try {
                mockMvc.perform(
                       MockMvcRequestBuilders.fileUpload("/api/file/avatar")
                               .file(file)
                               .cookie(util.getAlreadyLoginUserCookie())
                               .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                               .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(), CoreMatchers.instanceOf(ParamsErrorException.class));
            }
        }

        util.printSuccessMessage();
    }
}
