package org.neusoft.neubbs.controller.api;

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
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 管理用户 api
 */
@Controller
@RequestMapping("/api")
public class UserInfoCollector {

    @Autowired
    IUserService userService;

    /**
     * 输入 username ，获取用户信息
     * @param username
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoByName(@RequestParam(value = "username", required = false)String username) throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        //空判断
        if(username == null || username.length() < 0){
            responseJson.putAjaxFail(UserInfo.GET_USERINFO_USERNAME_NONULL);
            return responseJson;
        }

        //数据库获取数据
        UserDO user = userService.getUserByName(username);
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user);
        if(userInfoMap == null){
            responseJson.putAjaxFail(UserInfo.DATABASE_NO_EXIST_USER);
            return responseJson;
        }

        responseJson.putAjaxSuccess(UserInfo.GET_USERINFO_SUCCESS);
        responseJson.getModel().add(userInfoMap);

        return responseJson;
    }


    /**
     * 输入 username password email，注册用户
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/userinfo/register")
    @ResponseBody
    public ResponseJsonDTO registerUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter(UserInfo.USERNAME);
        String password = request.getParameter(UserInfo.PASSWORD);
        String email = request.getParameter(UserInfo.EMAIL);

        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if (username == null || username.length() == 0) {
            responseJson.putAjaxFail(UserInfo.REGISTER_USER_USERNAME_NONULL);
            return responseJson;
        }
        if (password == null || username.length() == 0) {
            responseJson.putAjaxFail(UserInfo.REGISTER_USER_PASSWORD_NONULL);
            return responseJson;
        }
        if (email == null || username.length() == 0) {
            responseJson.putAjaxFail(UserInfo.REGISTER_USER_EMAIL_NONULL);
            return responseJson;
        }

        //判断用户名唯一
        UserDO user = userService.getUserByName(username);
        if(user != null){
            responseJson.putAjaxFail(UserInfo.DATABASE_ALREAD_EXIST_USER);
            return responseJson;
        }

        //注册操作
        user = new UserDO();
            user.setName(username);
            user.setPassword(password);
            user.setEmail(email);
        int effectRow = userService.registerUser(user);
        if (effectRow == 0) {
            responseJson.putAjaxFail(UserInfo.REGISTER_USER_FAIL);
            return responseJson;
        }

        //注册成功,会自动注入用户 id（用户自增）
        int newId = user.getId();
        user = userService.getUserById(newId);//根据 id 重新查询用户

        responseJson.getModel().add(JsonUtils.getMapByObject(user));
        responseJson.putAjaxSuccess(UserInfo.REGISTER_USER_SUCCESS);
        return responseJson;
    }

    /**
     * 输入 username，password ，修改用户密码
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/userinfo/update-password")
    @ResponseBody
    public ResponseJsonDTO updateUserPasswordById(@RequestParam(value = "username", required = false)String username,
                                                  @RequestParam(value = "password", required = false)String password) throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if(username == null || username.length() == 0 ){
            responseJson.putAjaxFail(UserInfo.UPDATE_USER_PASSWORD_USERNAME_NONULL);
            return responseJson;
        }
        if(password == null || password.length() == 0){
            responseJson.putAjaxFail(UserInfo.UPDATE_USER_PASSWORD_PASSWORD_NONULL);
            return responseJson;
        }

        //更新用户密码
        int effectRow = userService.updateUserPasswordByName(username, password);
        if (effectRow == 0) {
            responseJson.putAjaxFail(UserInfo.DATABASE_NO_EXIST_USER);
            return responseJson;
        }

        responseJson.putAjaxSuccess(UserInfo.UPDATE_USER_PASSWORD_SUCCESS);
        return  responseJson;
    }

    /**
     * 输入 username rank ，修改用户权限
     * @param username
     * @param rank
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/userinfo/update-rank")
    @ResponseBody
    public ResponseJsonDTO updateUserRankById(@RequestParam(value = "username", required = false)String username,
                                                  @RequestParam(value = "rank", required = false)String rank) throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if(username == null || username.length() == 0){
            responseJson.putAjaxFail(UserInfo.UPDATE_USER_RANK_USERNAME_NONULL);
            return responseJson;
        }
        if(rank == null || rank.length() <= 0){
            responseJson.putAjaxFail(UserInfo.UPDATE_USER_RANK_RANK_NONULL);
            return responseJson;
        }

        //更新用户密码
        int effectRow = userService.updateUserRankByName(username, rank);
        if (effectRow == 0) {
            responseJson.putAjaxFail(UserInfo.DATABASE_NO_EXIST_USER);
            return responseJson;
        }

        responseJson.putAjaxSuccess(UserInfo.UPDATE_USER_RANK_SUCCESS);
        return  responseJson;
    }
}
