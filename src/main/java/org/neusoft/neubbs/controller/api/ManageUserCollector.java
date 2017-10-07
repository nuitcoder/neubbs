package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.JsonUtils;
import org.neusoft.neubbs.util.PatternUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理用户 api
 */
@Controller
@RequestMapping("/api")
public class ManageUserCollector {

    @Autowired
    IUserService userService;

    /**
     * 输入 username password email，注册用户
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/register")
    @ResponseBody
    public ResponseJsonDTO registerUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter(UserInfo.USERNAME);
        String password = request.getParameter(UserInfo.PASSWORD);
        String email = request.getParameter(UserInfo.EMAIL);

        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if (username == null) {
            responseJson.putAjaxFail(UserInfo.REGISTER_USERNAME_NONULL);
            return responseJson;
        }
        if (password == null) {
            responseJson.putAjaxFail(UserInfo.REGISTER_PASSWORD_NONULL);
            return responseJson;
        }
        if (email == null) {
            responseJson.putAjaxFail(UserInfo.REGISTER_EMAIL_NONULL);
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
            responseJson.putAjaxFail(UserInfo.REGISTER_FAIL);
            return responseJson;
        }

        //注册成功返回新用户 id
        int newId = user.getId();
        user = userService.getUserById(newId);//根据 id 重新查询用户

        responseJson.getModel().add(JsonUtils.getMapByObject(user));
        responseJson.putAjaxSuccess(UserInfo.REGISTER_SUCCESS);
        return responseJson;
    }

    /**
     * 输入id，删除用户
     * @param id
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/remove")
    @ResponseBody
    public ResponseJsonDTO removeUser(@RequestParam(value = "id", required = false)String id) throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if(id == null){
            responseJson.putAjaxFail(UserInfo.REMOVE_ID_NONULL);
            return responseJson;
        }

        if(!PatternUtils.pureNumber(id)){
            responseJson.putAjaxFail(UserInfo.ID_STYLE_ERROR_NO_PPURENUMBER);
            return responseJson;
        }

        //删除用户
        int effectRow = userService.removeUser(Integer.parseInt(id));
        if (effectRow == 0) {
            responseJson.putAjaxFail(UserInfo.DATABASE_NO_EXIST_USER);
            return responseJson;
        }

        responseJson.putAjaxSuccess(UserInfo.REMOVE_SUCCESS);
        return responseJson;
    }

    /**
     * 输入 id，password ，修改用户密码
     * @param id
     * @param password
     * @return
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/update-password")
    @ResponseBody
    public ResponseJsonDTO updateUserPasswordById(@RequestParam(value = "id", required = false)String id,
                                                  @RequestParam(value = "password", required = false)String password) throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if(id == null){
            responseJson.putAjaxFail(UserInfo.UPDATE_PASSWORD_ID_NONULL);
            return responseJson;
        }
        if(password == null || password.length() <= 0){
            responseJson.putAjaxFail(UserInfo.UPDATE_PASSWORD_PASSWORD_NONULL);
        }

        if(!PatternUtils.pureNumber(id)){
            responseJson.putAjaxFail(UserInfo.ID_STYLE_ERROR_NO_PPURENUMBER);
            return responseJson;
        }

        //更新用户密码
        int effectRow = userService.updateUserPasswordById(password, Integer.parseInt(id));
        if (effectRow == 0) {
            responseJson.putAjaxFail(UserInfo.DATABASE_NO_EXIST_USER);
            return responseJson;
        }

        responseJson.putAjaxSuccess(UserInfo.ALTER_PASSWORD_SUCCESS);
        return  responseJson;
    }

    /**
     * 输入 id，rank ，修改用户权限
     * @param id
     * @param rank
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/update-rank")
    @ResponseBody
    public ResponseJsonDTO updateUserRankById(@RequestParam(value = "id", required = false)String id,
                                                  @RequestParam(value = "rank", required = false)String rank) throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if(id == null){
            responseJson.putAjaxFail(UserInfo.UPDATE_RANK_ID_NONULL);
            return responseJson;
        }
        if(rank == null || rank.length() <= 0){
            responseJson.putAjaxFail(UserInfo.UPDATE_RANK_RANK_NONULL);
            return responseJson;
        }

        if(!PatternUtils.pureNumber(id)){
            responseJson.putAjaxFail(UserInfo.ID_STYLE_ERROR_NO_PPURENUMBER);
            return responseJson;
        }

        //更新用户密码
        int effectRow = userService.updateUserRankById(rank, Integer.parseInt(id));
        if (effectRow == 0) {
            responseJson.putAjaxFail(UserInfo.DATABASE_NO_EXIST_USER);
            return responseJson;
        }

        responseJson.putAjaxSuccess(UserInfo.ALTER_RANK_SUCCESS);
        return  responseJson;
    }
}
