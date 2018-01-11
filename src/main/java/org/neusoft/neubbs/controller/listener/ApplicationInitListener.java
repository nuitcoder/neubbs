package org.neusoft.neubbs.controller.listener;

import org.neusoft.neubbs.constant.api.ParamConst;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 应用初始化监听器
 *      - 初始化已登陆人数 0
 *
 * @author Suvan
 */
@WebListener
public class ApplicationInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();

        context.setAttribute(ParamConst.LOGIN_USER, 0);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) { }
}
