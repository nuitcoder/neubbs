package org.neusoft.neubbs.controller.listener;

import org.neusoft.neubbs.constant.api.ParamConst;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *  Api Session 监听器（监听 Session 的创建与销毁）
 *
 *  @author Suvan
 */
public class ApiSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        ServletContext context = httpSessionEvent.getSession().getServletContext();

        //在线访问人数 +1
        int onlineVisitUser = (int) context.getAttribute(ParamConst.COUNT_VISIT_USER);
        context.setAttribute(ParamConst.COUNT_VISIT_USER, ++onlineVisitUser);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ServletContext context = httpSessionEvent.getSession().getServletContext();

        //在线访问人数 -1
        int onlineVisitUser = (int) context.getAttribute(ParamConst.COUNT_VISIT_USER);
        context.setAttribute(ParamConst.COUNT_VISIT_USER, --onlineVisitUser);
    }
}
