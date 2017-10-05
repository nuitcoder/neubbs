package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.user.UserInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 获取信息 api
 */
@Controller
@RequestMapping("/api")
public class GetMessageCollector {

    @Autowired
    IUserService userService;
    @Autowired
    IRedisService redisService;


    @LoginAuthorization
    @RequestMapping(value = "/get/userinfo",method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getUserInfo(@RequestParam(value = "username",required = false)String username) throws Exception{
        ResponseJsonDTO responseJson = new ResponseJsonDTO();

        //空判断
        if(username == null || username.length() < 0){
            responseJson.put(AjaxRequestStatus.FAIL, UserInfo.GET_USERINFO_USERNAME_NONUL);
            return responseJson;
        }

        //方式1：数据库中获取
        Map<String,String> userInfoMap = userService.listUserInfoByName(username);
        if(userInfoMap == null){
            responseJson.put(AjaxRequestStatus.FAIL, LoginInfo.USER_NOEXIT);
            return responseJson;
        }
        responseJson.put(AjaxRequestStatus.SUCCESS, LoginInfo.USER_GETINFO_SUCCESS, userInfoMap);

        //方式2：从Redis缓存中获取
        //String userInfoString = redisService.getValueByKey(username);
        //responseJson.put(AjaxRequestStatus.SUCCESS, UserInfo.GETINFO_SUCCESS,JsonUtils.getMapByJSON(userInfoString));

        return responseJson;
    }
}