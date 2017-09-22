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
	 * 默认跳转到 index.jsp
	 * @param model Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = { "/*", "/*/*" })
	public String index(Model model) throws Exception {
		return "index";
	}
}