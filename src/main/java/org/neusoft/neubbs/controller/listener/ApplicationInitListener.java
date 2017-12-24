package org.neusoft.neubbs.controller.listener;

import org.neusoft.neubbs.constant.api.ParamConst;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 应用初始化监听器
 *      - ServletContext
 *
 * @author Suvan
 */
@WebListener
public class ApplicationInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();

        //在线访问人数
        context.setAttribute(ParamConst.VISIT_USER, 0);

        //已登录人数
        context.setAttribute(ParamConst.LOGIN_USER, 0);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
