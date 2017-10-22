package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.AccountInfo;
import org.neusoft.neubbs.constant.api.FileInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.FileUploadException;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.CookieUtils;
import org.neusoft.neubbs.util.JwtTokenUtils;
import org.neusoft.neubbs.util.PatternUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 文件 api
 *      1.上传用户头像
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private IUserService userService;

    private final String USER_IMAGE_PATH = "/WEB-INF/file/user/image/";

    /**
     * 1.上传用户头像（新上传的会覆盖旧的）
     *
     * @param multipartFile 用户上传的文件对象
     * @param request http请求
     * @return ResponseJsonDTO 传输对象，api 显示结果
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/upload-user-image", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO uploadUserImage(@RequestParam("image")MultipartFile multipartFile,
                                           HttpServletRequest request) throws Exception{
        /*
         * 文件上传
          *     1.判断上传文件是否为空
          *     2.判断文件类型
          *     3.检测文件大小，判断是否进行文件压缩处理（图片大于 1 MB 时）（Multipart 解析器 默认限制最大 5MB）
          *     4.设置文件根路径
          *     5.设置保存文件名（获取 Cookie 内 token ，拼接，用户id-用户名-图片名.后缀）
          *     6.构建 File 对象，判断父目录（不存在则创建，n 级）（File.separator 文件分隔符，可在文件前再次设置目录）
          *     7.上传文件，传内容，至服务器文件
          *     8.将文件名存入数据库指定用户
         */
        if (multipartFile.isEmpty()) {
            // 抛出文件空异常
            throw new FileUploadException(FileInfo.NO_CHOICE_PICTURE).log(LogWarnInfo.USER_NO_CHOICE_UPLOAD_FILE);
        }
        if (!PatternUtils.matchUserImage(multipartFile.getContentType())) {
            //抛出文件类型不匹配异常
            throw new FileUploadException(FileInfo.PICTURE_FORMAT_WRONG).log( multipartFile.getContentType() + LogWarnInfo.FILE_TYPE_NO_USER_IMAGE_SPECIFY_TYPE);
        }
        if(multipartFile.getSize() >= FileInfo.SIZE_ONE_MB){
            //文件压缩处理

        }

        String serverPath = request.getServletContext().getRealPath(this.USER_IMAGE_PATH);

        String authentication = CookieUtils.getCookieValue(request, AccountInfo.AUTHENTICATION);
        UserDO user = JwtTokenUtils.verifyToken(authentication, SecretInfo.TOKEN_SECRET_KEY);

        String fileName = user.getId() + "_" + user.getName() + "_" + multipartFile.getOriginalFilename();
        File imageFile = new File(serverPath, fileName);
        if (!imageFile.getParentFile().exists()) {
            imageFile.getParentFile().mkdirs();
        }

        multipartFile.transferTo(imageFile);

        if (!userService.uploadUserImage(user.getName(), fileName)) {
            if(!imageFile.delete()){
               //删除失败
               throw new FileUploadException(FileInfo.UPLOAD_FAIL).log(LogWarnInfo.USER_IMAGE_SAVE_DATABASE_FAIL + LogWarnInfo.DELETE_IMAGE_FILE_FAIL);
            }

            throw new FileUploadException(FileInfo.UPLOAD_FAIL).log(LogWarnInfo.USER_IMAGE_SAVE_DATABASE_FAIL);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, FileInfo.UPLOAD_SUCCESS);
    }
}
