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

    /**
     * Session 创建时调用
     * @param httpSessionEvent httpSession事件
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //获取全局上下文
        ServletContext application = httpSessionEvent.getSession().getServletContext();

        Integer onlineUser = (Integer) application.getAttribute(ParamConst.COUNT_VISIT_USER);

        if (onlineUser == null) {
            onlineUser = 0;
        }

        //在线访问用户 +1
        onlineUser++;

        application.setAttribute(ParamConst.COUNT_VISIT_USER, onlineUser);
    }

    /**
     * Session 销毁时调用
     * @param httpSessionEvent httpSession事件
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ServletContext application = httpSessionEvent.getSession().getServletContext();

        Integer onlineUser = (Integer) application.getAttribute(ParamConst.COUNT_VISIT_USER);

        if (onlineUser == null) {
            onlineUser = 0;
        } else {
            //在线访问人数-1
            onlineUser--;
        }

        application.setAttribute(ParamConst.COUNT_VISIT_USER, onlineUser);
    }
}
