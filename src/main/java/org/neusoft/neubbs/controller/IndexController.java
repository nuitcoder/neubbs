package org.neusoft.neubbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 论坛总控制器
 */
@Controller
public class IndexController {

	/**
	 * 获取项目根目录
	 * @param request HttpServletRequest
	 * @return String
	 */
	private String getBastPath(HttpServletRequest request) {
		String url = "";
		url += request.getScheme() + "://" + request.getServerName();

		int port = request.getServerPort();
		if (port != 80) {
			url += ":" + port;
		}

		url += request.getContextPath();
		return url;
	}
	

	/**
	 * 首页跳转
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, Model model) throws Exception {
		String basePath = getBastPath(request);
		model.addAttribute("basePath", basePath);

		return "../../index";
	}
}