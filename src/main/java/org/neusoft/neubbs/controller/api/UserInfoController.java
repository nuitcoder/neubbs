package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.AjaxRequestStatus;
import org.neusoft.neubbs.constant.UserInfo;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 注册，用户信息，修改密码，修改权限 api
 */
@Controller
@RequestMapping("/api")
public class UserInfoController {

    @Autowired
    IUserService userService;

    /**
     * 输入 username password email，注册用户
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO registerUser(HttpServletRequest request) throws Exception {
        String username = request.getParameter(UserInfo.USERNAME);
        String password = request.getParameter(UserInfo.PASSWORD);
        String email = request.getParameter(UserInfo.EMAIL);

        if (username == null || username.length() == 0) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.REGISTER_USER_USERNAME_NONULL);
        }
        if (password == null || username.length() == 0) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.REGISTER_USER_PASSWORD_NONULL);
        }
        if (email == null || username.length() == 0) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.REGISTER_USER_EMAIL_NONULL);
        }

        //判断用户名唯一
        UserDO user = userService.getUserByName(username);
        if (user != null) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.DATABASE_ALREAD_EXIST_USER);
        }

        //注册操作
        user = new UserDO();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        int effectRow = userService.registerUser(user);
        if (effectRow == 0) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.REGISTER_USER_FAIL);
        }

        //注册成功,会自动注入用户 id（用户自增）
        int newId = user.getId();
        user = userService.getUserById(newId);//根据 id 重新查询用户
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, UserInfo.REGISTER_USER_SUCCESS, userInfoMap);
    }

    /**
     * 输入 username ，获取用户信息
     * @param username
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoByName(@RequestParam(value = "username", required = false)String username) throws Exception{
        //空判断
        if(username == null || username.length() < 0){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.GET_USERINFO_USERNAME_NONULL);
        }

        //数据库获取数据
        UserDO user = userService.getUserByName(username);
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user);
        if(userInfoMap == null){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.DATABASE_NO_EXIST_USER);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, UserInfo.GET_USERINFO_SUCCESS, userInfoMap);
    }

    /**
     * 输入 username，password ，修改用户密码
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/user/update-password", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO updateUserPasswordById(@RequestParam(value = "username", required = false)String username,
                                                  @RequestParam(value = "password", required = false)String password) throws Exception{
        if(username == null || username.length() == 0 ){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.UPDATE_USER_PASSWORD_USERNAME_NONULL);
        }
        if(password == null || password.length() == 0){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.UPDATE_USER_PASSWORD_PASSWORD_NONULL);
        }

        //更新用户密码
        int effectRow = userService.updateUserPasswordByName(username, password);
        if (effectRow == 0) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.DATABASE_NO_EXIST_USER);
        }

        return new  ResponseJsonDTO(AjaxRequestStatus.SUCCESS, UserInfo.UPDATE_USER_PASSWORD_SUCCESS);
    }

    /**
     * 输入 username rank ，修改用户权限
     * @param username
     * @param rank
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/user/update-rank", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO updateUserRankById(@RequestParam(value = "username", required = false)String username,
                                                  @RequestParam(value = "rank", required = false)String rank) throws Exception{
        if(username == null || username.length() == 0){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.UPDATE_USER_RANK_USERNAME_NONULL);
        }
        if(rank == null || rank.length() <= 0){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.UPDATE_USER_RANK_RANK_NONULL);
        }

        //更新用户密码
        int effectRow = userService.updateUserRankByName(username, rank);
        if (effectRow == 0) {
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, UserInfo.DATABASE_NO_EXIST_USER);
        }

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, UserInfo.UPDATE_USER_RANK_SUCCESS);
    }
}
