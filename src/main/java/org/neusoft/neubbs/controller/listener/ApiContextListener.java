package org.neusoft.neubbs.controller.listener;

import org.neusoft.neubbs.constant.api.ParamConst;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Api 上下文（全局）监听器
 *
 * @author Suvan
 */
public class ApiContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();

        //在线访问人数
        context.setAttribute(ParamConst.COUNT_VISIT_USER, 0);

        //已登录人数
        context.setAttribute(ParamConst.COUNT_LOGIN_USER, 0);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
