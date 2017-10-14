package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.count.CountInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统计 api
 *      1.在线访问人数
 *      2.在线登录人数
 */
@Controller
@RequestMapping("/api/count/")
public class CountController {
    /**
     * 1.在线访问人数
     * @param request
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/online-visit-user")
    @ResponseBody
    public ResponseJsonDTO onlineVisitUser(HttpServletRequest request) throws Exception{
        Integer onlineVisitUser = (Integer) request.getServletContext().getAttribute(CountInfo.ONLINE_VISIT_USER);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, CountInfo.ONLINE_VISIT_USER_SUCCESS,
                                   CountInfo.ONLINE_VISIT_USER, onlineVisitUser);
    }

    /**
     * 2.在线登录人数
     * @param request
     * @return ResponseJsonDTO
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/online-login-user")
    @ResponseBody
    public ResponseJsonDTO onlineLoginUser(HttpServletRequest request) throws Exception{
        Integer onlineLoginUser = (Integer)request.getServletContext().getAttribute(CountInfo.ONLINE_LOGIN_USER);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, CountInfo.ONLINE_LOGIN_USER_SUCCESS,
                                   CountInfo.ONLINE_LOGIN_USER, onlineLoginUser);
    }
}
