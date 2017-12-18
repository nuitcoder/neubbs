package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.dto.PageJsonDTO;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统计 api
 *      - 访问人数
 *      - 登录人数
 *      - 用户总数
 *      - 话题总数
 *      - 回复总数
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/count")
public class CountController {

    private final IHttpService httpService;
    private final IUserService userService;
    private final ITopicService topicService;

    @Autowired
    public CountController(IHttpService httpService, IUserService userService,
                           ITopicService topicService) {
        this.httpService = httpService;
        this.userService = userService;
        this.topicService = topicService;
    }

    /**
     * 获取统计信息（CountController 默认访问）
     *      - 用户总数
     *      - 话题总数
     *      - 回复总数
     *
     * @param request http默认请求
     * @return PageJsonDTO 页面传输对象
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public PageJsonDTO count(HttpServletRequest request) {
        Map<String, Object> modelMap = new LinkedHashMap<>(SetConst.SIZE_THREE);
            modelMap.put(ParamConst.USER, userService.countUserTotals());
            modelMap.put(ParamConst.TOPIC, topicService.countTopicTotals());
            modelMap.put(ParamConst.REPLY, topicService.countReplyTotals());
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, modelMap);
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
}
