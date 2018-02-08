package test.org.neusoft.neubbs.util;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.exception.FtpException;
import org.neusoft.neubbs.utils.FtpUtil;

import java.io.IOException;
import java.util.List;

/**
 * FtpUtil 测试类
 *      - 测试 createDirectory()
 *      - 测试 listServerPathFileName()
 *      - 测试 uploadFile()
 *      - 测试 delete()
 *      - 测试 deleteDirectory()
 *
 * @author Suvan
 */
@RunWith(JUnit4.class)
public class FtpUtilTest {

    /**
     * 测试 createDirectory()
     */
    @Ignore
    public void testCreateDirectory() throws IOException {
        String[] paths = {"test","/test/test1", "test/test2/", "test/test3"};

        //create directory
        for (String path: paths) {
            FtpUtil.createDirectory(path);
        }
    }

    /**
     * 测试 listServerPathFileName()
     */
    @Ignore
    public void testListDirectoryFileName() throws IOException {
        List<String> fileNameList = FtpUtil.listServerPathFileName("/user/default/");
        for (String name: fileNameList) {
            System.out.println("filename: " + name);
        }
    }

    /**
     * 测试 uploadFile()
     */
    @Ignore
    public void testUploadFile() throws IOException, FtpException {
        //file stream
        //File file = new File("D:\\suvan.jpeg");
        //FileInputStream fileInputStream = new FileInputStream(file);

        //byte steam
        byte [] bytes = new byte[98888];
        ByteInputStream byteInputStream = new ByteInputStream(bytes, bytes.length);


        String serverFilePath = "/testUser/user";
        FtpUtil.createDirectory(serverFilePath);
        FtpUtil.uploadFile(serverFilePath, "avatar.png", byteInputStream);
    }

    /**
     * 测试 delete()
     */
    @Ignore
    public void testDelete() throws IOException {
        //FtpUtil.delete("testUser/willDelete", null);
        FtpUtil.delete("testUser/user/", "avatar.png");
    }

    /**
     * 测试 deleteDirectory()
     */
    @Ignore
    public void testDirectory() throws IOException {
        FtpUtil.deleteDirectory("testUser");
    }
}
