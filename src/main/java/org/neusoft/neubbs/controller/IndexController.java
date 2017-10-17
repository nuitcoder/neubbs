package org.neusoft.neubbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页控制器
 *
 * @author Ahonn
 */
@Controller
public class IndexController {
	/**
	 * 默认跳转到 index.jsp
	 *
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = { "/*", "/*/*" })
	public String index() throws Exception {
		return "index";
	}
}