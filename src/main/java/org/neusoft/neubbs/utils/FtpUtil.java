package org.neusoft.neubbs.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

/**
 * FTP 工具类
 *      - 创建目录
 *      - 上传文件
 *      - 删除文件
 *      - 删除目录
 *      - 退出连接
 *
 * @author Suvan
 */
public final class FtpUtil {
    private FtpUtil() { }

    private static final String URL = "119.29.192.62";
    private static final int PORT = 21;
    private static final String USERNAME = "neubbs";
    private static final String PASSWORD = "ABliushuwei123";

    private static FTPClient ftpClient;

    private static FTPClient getFtpClientInstance() throws IOException {
        if (ftpClient == null) {
            ftpClient = new FTPClient();

            ftpClient.connect(URL, PORT);
            ftpClient.login(USERNAME, PASSWORD);

            //二进制方式传输
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                throw new IOException("FTP 服务器连接失败!");
            }
        }

        return ftpClient;
    }

    /**
     * 创建目录
     *
     * @param serverDirectoryPath 待创建的服务器目录路径（举例：/user/5-suvan/avator）
     */
    public static void createDirectory(String serverDirectoryPath) throws IOException {
        String[] paths = serverDirectoryPath.split("/");
        for (String path: paths) {
            if ("".equals(path)) {
                continue;
            }

            getFtpClientInstance().makeDirectory(path);
            ftpClient.changeWorkingDirectory(path);
        }

        //return root path
        ftpClient.changeWorkingDirectory("/");
    }

    /**
     * 上传文件
     *      - 创建文件目录，并切换文件目录准备上传
     *      - 上传文件
     *
     * @param serverFilePath FTP服务器保存路径（根路径为: /）
     * @param serverFileName 上传到FTP服务器保存文件的名称
     * @param fileInputStream 待上传文件的输入流（可传入 new FileInputStream()）
     */
    public static void uploadFile(String serverFilePath, String serverFileName, InputStream fileInputStream)
            throws Exception {

        getFtpClientInstance().changeWorkingDirectory(serverFilePath);

        ftpClient.storeFile(serverFileName, fileInputStream);

        fileInputStream.close();
    }

    /**
     * 删除指定文件
     *
     * @param filePath 路径
     * @param fileName 文件名（若删除目录，此字符串可为 null）
     * @throws IOException IO异常
     */
    public static void deleteFile(String filePath, String fileName) throws IOException {
        String name = "/".equals(filePath.substring(filePath.length()))
                ? filePath + fileName : filePath + "/" + fileName;

        getFtpClientInstance().deleteFile(name);
    }

    /**
     * 删除目录（及内部文件夹,文件）
     *
     * @param directoryPath 指定要删除的目录
     * @throws IOException IO异常
     */
    public static void deleteDirectory(String directoryPath) throws IOException {
        directoryPath = "/".equals(directoryPath.substring(directoryPath.length()))
                ? directoryPath.substring(0, directoryPath.length() - 1) : directoryPath;

        FTPFile[] files = getFtpClientInstance().listFiles(directoryPath);
        if (files != null && files.length >= 0) {
            for (FTPFile file: files) {
                if (file.isDirectory()) {
                    //递归删除目录所有文件
                    deleteDirectory(directoryPath + "/" + file.getName());

                    //切换到父目录，删除文件夹（不切换无法删除目录）
                    ftpClient.changeWorkingDirectory(directoryPath.substring(0, directoryPath.lastIndexOf("/")));
                    ftpClient.removeDirectory(directoryPath);
                } else {
                    //删除特定文件
                    ftpClient.deleteFile(directoryPath + "/" + file.getName());
                }
            }
        }

        ftpClient.changeWorkingDirectory(directoryPath.substring(0, directoryPath.lastIndexOf("/")));
        ftpClient.removeDirectory(directoryPath);
    }

    /**
     * 退出连接
     *
     * @throws IOException IO异常
     */
    public static void destory() throws IOException {
        if (ftpClient != null) {
            ftpClient.logout();
            ftpClient.disconnect();
            ftpClient = null;
        }
    }
}
