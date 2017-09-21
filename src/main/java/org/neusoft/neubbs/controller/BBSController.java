package org.neusoft.neubbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 论坛总控制器【控制页面跳转】
 */
@Controller
public class BBSController {



	/************************************返回视图(一级页面)*****************************************/
	/*获取项目根路径*/
	private String getBastPath(HttpServletRequest request) {
		//项目根路径：http://localhost:8080/blog
		return request.getScheme() + "://" +
				request.getServerName() + ":" +
				request.getServerPort() +
				request.getContextPath() + "/";
	}


	/*首页跳转*/
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, Model model) throws Exception {
		//1.获取数据

		//2.设置Model
		String basePath = getBastPath(request);
		model.addAttribute("basePath", basePath);//项目根路径


		//3.跳到index.jsp
		return "../../index";
	}


}