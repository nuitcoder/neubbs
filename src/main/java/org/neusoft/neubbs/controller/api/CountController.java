package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.dto.PageJsonDTO;
import org.neusoft.neubbs.service.IHttpService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final IHttpService httpService;

    @Autowired
    public CountController(IHttpService httpService) {
        this.httpService = httpService;
    }

    /**
     * 1.在线访问人数1
     *
     * @param request http请求
     * @return PageJsonDTO 响应JSON传输对象
     */
    @RequestMapping(value = "/visit")
    @ResponseBody
    public PageJsonDTO onlineVisitUser(HttpServletRequest request) {
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                ParamConst.COUNT_VISIT_USER, httpService.getOnlineVisitUserNumber(request));
    }

    /**
     * 2.在线登录人数
     *
     * @param request http请求
     * @return PageJsonDTO 响应JSON传输对象
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public PageJsonDTO onlineLoginUser(HttpServletRequest request) throws Exception {
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                ParamConst.COUNT_LOGIN_USER, httpService.getOnlineLoginUserNumber(request));
    }
}
