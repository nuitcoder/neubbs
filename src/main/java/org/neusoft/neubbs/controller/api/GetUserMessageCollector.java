package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.JsonUtils;
import org.neusoft.neubbs.util.PatternUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 获取用户信息 api
 */
@Controller
@RequestMapping("/api")
public class GetUserMessageCollector {

    @Autowired
    IUserService userService;
    @Autowired
    IRedisService redisService;

    /**
     * 输入 id 获取用户信息
     * @param id
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/get/userinfo-id", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoById(@RequestParam(value = "id", required = false)String id) throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if(id == null){
            responseJson.putAjaxFail(UserInfo.GET_USERINFO_ID_NONULL);
            return responseJson;
        }

        if(!PatternUtils.pureNumber(id)) {
            responseJson.putAjaxFail(UserInfo.ID_STYLE_ERROR_NO_PPURENUMBER);
            return responseJson;
        }

        UserDO user = userService.getUserById(Integer.parseInt(id));
        Map<String, Object> userInfoMap = JsonUtils.getMapByObject(user);
        if (userInfoMap == null) {
            responseJson.putAjaxFail(UserInfo.DATABASE_NO_EXIST_USER);
            return responseJson;
        }

        responseJson.putAjaxSuccess(UserInfo.GET_USERINFO_SUCCESS);
        responseJson.getModel().add(userInfoMap);

        return responseJson;
    }

    /**
     * 输入 username 获取用户信息
     * @param username
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/get/userinfo-name", method = RequestMethod.GET)
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
     * 获取所有管理员用户
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/get/userinfo-adminuser" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoAllAdminUser() throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        List<UserDO> userList = userService.getAllAdminUser();
        if (userList.size() <= 0) {
            responseJson.putAjaxFail(UserInfo.NO_EXIST_ADMINUSER);
        }

        //返回多个参数
        Map<String, Object> userInfoMap = null;
        for(UserDO user: userList){
            userInfoMap = JsonUtils.getMapByObject(user);
            responseJson.getModel().add(userInfoMap);
        }

        responseJson.putAjaxSuccess(UserInfo.GET_ALL_ADMININFO_SUCCESS);

        return responseJson;
    }

    /**
     * 获取指定日期注册用户
     * @param year
     * @param month
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/get/userinfo-assigindate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoAssiginDateByYearMonth(@RequestParam(value = "year", required = false)String year,
                                                             @RequestParam(value = "month", required = false)String month)
                                                                 throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        if(year == null || year.length() <= 0){
           responseJson.putAjaxFail(UserInfo.GET_USERINFO_YEAR_NONULL);
           return responseJson;
        }
        if(month == null || month.length() <= 0){
            responseJson.putAjaxFail(UserInfo.GET_USERINFO_MONTH_NONULL);
            return responseJson;
        }
        if (!PatternUtils.pureNumber(year)) {
           responseJson.putAjaxFail(UserInfo.GET_USERINFO_YEAR_STYLE_ERROR);
           return responseJson;
        }
        if (!PatternUtils.pureNumber(month)) {
            responseJson.putAjaxFail((UserInfo.GET_USERINFO_MONTH_STYLE_ERROR));
            return responseJson;
        }

       List<UserDO> userList = userService.getAssiginDateRegisterUserByYearMonth(Integer.parseInt(year), Integer.parseInt(month));
       if(userList.size() == 0){
           responseJson.putAjaxSuccess(UserInfo.ASSIGIN_DATE_NO_EXIST_REGISTER_USER);
           return responseJson;
       }

       Map<String, Object> userInfoMap = null;
       for(UserDO user: userList){
          userInfoMap = JsonUtils.getMapByObject(user);
          responseJson.getModel().add(userInfoMap);
       }
       responseJson.putAjaxSuccess(UserInfo.GET_ASSIGIN_DATE_REGISTER_USERINFO_SUCCESS);

        return responseJson;
    }

    /**
     * 获取所有用户信息
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/get/userinfo-all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfoAllUser() throws Exception{
       ResponseJsonDTO responseJson = new ResponseJsonDTO();

       List<UserDO> userList = userService.getAllUser();

       Map<String, Object> userInfoMap = null;
       for(UserDO user: userList){
           userInfoMap = JsonUtils.getMapByObject(user);
           responseJson.getModel().add(userInfoMap);
       }

       responseJson.putAjaxSuccess(UserInfo.GET_ALL_USERINFO_SUCCESS);
       return responseJson;
    }
}