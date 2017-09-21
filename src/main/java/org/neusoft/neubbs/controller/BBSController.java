package org.neusoft.neubbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 论坛总控制器
 */
@Controller
public class BBSController {

	/**
	 * 获取项目根目录
	 * @param request HttpServletRequest
	 * @return String
	 */
	private String getBastPath(HttpServletRequest request) {
		return request.getScheme() + "://"
				+ request.getServerName() + ":"
				+ request.getServerPort()
				+ request.getContextPath() + "/";
	}


	/*首页跳转*/

	/**
	 * 跳转到 index.jsp
	 * @param request HttpServletRequest
	 * @param model Model
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, Model model) throws Exception {
		String basePath = getBastPath(request);
		model.addAttribute("basePath", basePath);

		return "../../index";
	}
}