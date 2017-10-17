package org.neusoft.neubbs.controller.listener;

import org.neusoft.neubbs.constant.api.CountInfo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 在线访问人数监听器（监听 Session 的创建与销毁）
 */
public class ApiSessionListener implements HttpSessionListener{

    /**
     * Session 创建时调用
     * @param httpSessionEvent
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //获取全局上下文
        ServletContext application = httpSessionEvent.getSession().getServletContext();

        Integer onlineUser = (Integer)application.getAttribute(CountInfo.ONLINE_VISIT_USER);

        if (onlineUser == null) {
            onlineUser = 0;
        }

        //在线访问用户+1
        onlineUser ++;

        application.setAttribute(CountInfo.ONLINE_VISIT_USER, onlineUser);
    }

    /**
     * Session 销毁时调用
     * @param httpSessionEvent
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ServletContext application = httpSessionEvent.getSession().getServletContext();

        Integer onlineUser = (Integer)application.getAttribute("onlineVisitUser");

        if (onlineUser == null) {
            onlineUser = 0;
        } else {
            //在线访问人数-1
            onlineUser --;
        }

        application.setAttribute(CountInfo.ONLINE_VISIT_USER, onlineUser);
    }
}
