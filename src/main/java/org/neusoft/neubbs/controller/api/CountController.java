package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.dto.PageJsonDTO;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统计 api
 *      - 访问人数
 *      - 登录人数
 *      - 用户总数
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/count/")
public class CountController {

    private final IHttpService httpService;
    private final IUserService userService;

    @Autowired
    public CountController(IHttpService httpService, IUserService userService) {
        this.httpService = httpService;
        this.userService = userService;
    }

    /**
     * 访问人数
     *
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "/visit")
    @ResponseBody
    public PageJsonDTO visit(HttpServletRequest request) {
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                ParamConst.VISIT_USER, httpService.getOnlineVisitUserNumber(request));
    }

    /**
     * 登录人数
     *
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public PageJsonDTO login(HttpServletRequest request) {
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                ParamConst.LOGIN_USER, httpService.getOnlineLoginUserNumber(request));
    }

    /**
     * 用户总数
     *
     * @return PageJsonDTO
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public PageJsonDTO user() {
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.USER_TOTALS, userService.countUserTotals());
    }
}
