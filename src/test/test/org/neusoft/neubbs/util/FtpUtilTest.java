package test.org.neusoft.neubbs.util;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.utils.FtpUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * FtpUtil 测试类
 *
 * @author Suvan
 */
@RunWith(JUnit4.class)
public class FtpUtilTest {
    /**
     * 测试创建指定目录
     */
    @Ignore
    public void testCreateDirectory() throws Exception {
        String[] paths = {
          "test","/test/test1", "test/test2/", "test/test3"
        };

        for (String serverFilePath: paths) {
            FtpUtil.createDirectory(serverFilePath);
        }
    }

    /**
     * 测试获取指定 FTP 服务器目录下, 文件集合
     */
    @Ignore
    public void testListDirectoryFileName() throws Exception {
        List<String> fileNameList = FtpUtil.listDirectoryFileName("/user/6-suvan/avator");
        for (String name: fileNameList) {
            System.out.println("文件名：" + name);
        }
    }

    /**
     * 测试上传文件
     */
    @Ignore
    public void testUploadFileToFtpRemoteStorage() throws Exception {
        File file = new File("D:\\suvan.png");
        String serverFilePath = "/testUser/user";

        FtpUtil.createDirectory(serverFilePath);
        FtpUtil.uploadFile(serverFilePath, "avator.png", new FileInputStream(file));
    }

    /**
     * 测试删除目录 or 文件
     *      - 删除目录的话，第二参数为 null
     */
    @Ignore
    public void testDelete() throws Exception {
        FtpUtil.delete("test/test3", "test.jpeg");
    }

    /**
     * 测试删除目录（及内部子文件夹,文件）
     */
    @Ignore
    public void tesDeleteDirectoryAllContent() throws Exception {
        FtpUtil.deleteDirectory("/test");
    }
}
