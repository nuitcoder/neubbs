package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.CountInfo;
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
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/count/")
public class CountController {

    /**
     * 1.在线访问人数
     *
     * @param request http请求
     * @return ResponseJsonDTO 传输对象，api 显示结果
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/visit")
    @ResponseBody
    public ResponseJsonDTO onlineVisitUser(HttpServletRequest request) throws Exception{
        Integer onlineVisitUser = (Integer) request.getServletContext().getAttribute(CountInfo.ONLINE_VISIT_USER);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, CountInfo.ONLINE_VISIT_USER, onlineVisitUser);
    }

    /**
     * 2.在线登录人数
     *
     * @param request http请求
     * @return ResponseJsonDTO 传输对象， api 显示结果
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/login")
    @ResponseBody
    public ResponseJsonDTO onlineLoginUser(HttpServletRequest request) throws Exception{
        Integer onlineLoginUser = (Integer)request.getServletContext().getAttribute(CountInfo.ONLINE_LOGIN_USER);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, CountInfo.ONLINE_LOGIN_USER, onlineLoginUser);
    }
}
