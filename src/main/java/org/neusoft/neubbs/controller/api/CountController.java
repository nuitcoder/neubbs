package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.AjaxRequestStatus;
import org.neusoft.neubbs.constant.CountInfo;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统计控制器
 */
@Controller
@RequestMapping("/api")
public class CountController {
    /**
     * 获取在线访问人数
     * @param request
     * @return
     * @throws Exception
     */
    @LoginAuthorization @AdminRank
    @RequestMapping(value = "/count/online/visit-user")
    @ResponseBody
    public ResponseJsonDTO onlineVisitUser(HttpServletRequest request) throws Exception{
        Integer onlineVisitUser = (Integer) request.getServletContext().getAttribute(CountInfo.ONLINE_VISIT_USER);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, CountInfo.ONLINE_VISIT_USER_SUCCESS,
                                   CountInfo.ONLINE_VISIT_USER, onlineVisitUser);
    }

    /**
     * 获取在线登录人数
     * @param request
     * @return
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "count/online/login-user")
    @ResponseBody
    public ResponseJsonDTO onlineLoginUser(HttpServletRequest request) throws Exception{
        Integer onlineLoginUser = (Integer)request.getServletContext().getAttribute(CountInfo.ONLINE_LOGIN_USER);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, CountInfo.ONLINE_LOGIN_USER_SUCCESS,
                                   CountInfo.ONLINE_LOGIN_USER, onlineLoginUser);
    }
}
