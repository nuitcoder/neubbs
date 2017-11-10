package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.FileUploadErrorException;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.entity.properties.NeubbsConfigDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.CookieUtil;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.neusoft.neubbs.utils.PatternUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


/**
 * 文件 api
 *      + 上传用户头像
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/file")
public class FileController {

    private final IUserService userService;
    private final NeubbsConfigDO neubbsConfig;

    /**
     * Constructor
     */
    @Autowired
    public FileController(IUserService userService, NeubbsConfigDO neubbsConfig) {
        this.userService = userService;
        this.neubbsConfig = neubbsConfig;
    }

    /**
     * 1.上传用户头像（新上传的会覆盖旧的）
     *
     * 业务流程
     *     1.判断上传文件是否为空
     *     2.判断文件类型
     *     3.检测文件大小，判断是否进行文件压缩处理（图片大于 1 MB 时）（Multipart 解析器 默认限制最大 5MB）
     *     4.设置文件根路径
     *     5.设置保存文件名（获取 Cookie 内 token ，拼接，用户id-用户名-图片名.后缀）
     *     6.构建 File 对象，判断父目录（不存在则创建，n 级）（File.separator 文件分隔符，可在文件前再次设置目录）
     *     7.上传文件，传内容，至服务器文件
     *     8.将文件名存入数据库指定用户
     *
     * @param multipartFile 用户上传的文件对象
     * @param request http请求
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws FileUploadErrorException 文件上传错误异常
     * @throws AccountErrorException 账户错误异常
     * @throws DatabaseOperationFailException 数据库操作异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO uploadUserImage(@RequestParam("image")MultipartFile multipartFile,
                                           HttpServletRequest request)
            throws FileUploadErrorException, AccountErrorException, DatabaseOperationFailException {

        if (multipartFile.isEmpty()) {
            // 抛出文件空异常
            throw new FileUploadErrorException(ApiMessage.NO_CHOICE_PICTURE).log(LogWarn.FILE_01);
        }
        String fileType = multipartFile.getContentType();
        if (!PatternUtil.matchUserImage(fileType)) {
            //抛出文件类型不匹配异常
            throw new FileUploadErrorException(ApiMessage.PICTURE_FORMAT_WRONG).log(fileType + LogWarn.FILE_02);
        }
//        if (multipartFile.getSize() >= SetConst.SIZE_ONE_MB) {
//            //文件压缩处理
//
//        }

        String serverPath = request.getServletContext().getRealPath(neubbsConfig.getUserImageUploadPath());

        String authentication = CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
        UserDO cookieUser = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
        if (cookieUser == null) {
            throw new AccountErrorException(ApiMessage.TOKEN_EXPIRED).log(LogWarn.ACCOUNT_05);
        }

        String fileName = cookieUser.getId()
                            + "_" + System.currentTimeMillis()
                            + "_" + multipartFile.getOriginalFilename();

        File imageFile = new File(serverPath, fileName);
        if (!imageFile.getParentFile().exists()) {
           //服务器检测不到目录，抛出异常
            throw new FileUploadErrorException(ApiMessage.NO_PARENT_DIRECTORY).log(LogWarn.FILE_03);
        }

        try {
            multipartFile.transferTo(imageFile);
        } catch (IOException e) {
            throw new FileUploadErrorException(ApiMessage.UPLOAD_FAIL).log(LogWarn.FILE_04);
        }

        userService.uploadUserImage(cookieUser.getName(), fileName);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ApiMessage.UPLOAD_SUCCESS);
    }
}
