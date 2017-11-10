package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
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
     * @return ResponseJsonDTO 响应JSON传输对象
     */
    @RequestMapping(value = "/visit")
    @ResponseBody
    public ResponseJsonDTO onlineVisitUser(HttpServletRequest request) {
        int onlineVisitUser = (int) request.getServletContext().getAttribute(ParamConst.COUNT_VISIT_USER);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.COUNT_VISIT_USER, onlineVisitUser);
    }

    /**
     * 2.在线登录人数
     *
     * @param request http请求
     * @return ResponseJsonDTO 响应JSON传输对象
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public ResponseJsonDTO onlineLoginUser(HttpServletRequest request) throws Exception {
        int onlineLoginUser = (int) request.getServletContext().getAttribute(ParamConst.COUNT_LOGIN_USER);
        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.COUNT_LOGIN_USER, onlineLoginUser);
    }
}
