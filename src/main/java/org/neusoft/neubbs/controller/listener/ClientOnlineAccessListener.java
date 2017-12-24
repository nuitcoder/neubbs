package org.neusoft.neubbs.controller.listener;

import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.utils.CookieUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;

/**
 *  客户端在线访问监听器
 *      - 监控 Session
 *      - 监控 Request
 *
 *  @author Suvan
 */
public class ClientOnlineAccessListener implements HttpSessionListener, ServletRequestListener {

    private HashMap<String, HttpSession> visitSessionMap = new HashMap<>();
    private HashMap<String, HttpSession> loggedSessionMap = new HashMap<>();

    private HttpServletRequest clientRequest;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        String ip = this.getRequestClientIp();

        //judge user login state
        if (isUserLogged()) {
            //login user +1
            if (loggedSessionMap.get(ip) == null) {
                HttpSession session = httpSessionEvent.getSession();
                loggedSessionMap.put(ip, session);
                this.updateApplicationLoggedUserCount(session.getServletContext());
            }
        } else {
            //visit user + 1
            if (visitSessionMap.get(ip) == null) {
                HttpSession session = httpSessionEvent.getSession();
                visitSessionMap.put(ip, session);
                this.updateApplicationVisitUserCount(session.getServletContext());
            }
        }

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        String ip = this.getRequestClientIp();

        //judge user login state
        if (isUserLogged()) {
            //login user -1
            if (loggedSessionMap.get(ip) != null) {
                loggedSessionMap.remove(ip);
                this.updateApplicationLoggedUserCount(httpSessionEvent.getSession().getServletContext());
            }
        } else {
            //login user +1
            if (visitSessionMap.get(ip) != null) {
                visitSessionMap.remove(ip);
                this.updateApplicationVisitUserCount(httpSessionEvent.getSession().getServletContext());
            }
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        clientRequest = (HttpServletRequest) servletRequestEvent.getServletRequest();
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 获取请求客户端 ip
     *
     * @return String 客户端 ip
     *
     */
    private String getRequestClientIp() {
       return clientRequest.getRemoteAddr();
    }

    /**
     * 应用在线用户统计(浏览，未登陆)
     *      - 根据 session 的维护请求 ip
     *
     * @param context 全局上下文对象
     */
    private void updateApplicationVisitUserCount(ServletContext context) {
        context.setAttribute(ParamConst.VISIT_USER, visitSessionMap.size());
    }

    private void updateApplicationLoggedUserCount(ServletContext context) {
        context.setAttribute(ParamConst.LOGIN_USER, loggedSessionMap.size());
    }

    /**
     * 判断用户是否已经登陆
     *
     * @return boolean 用户登陆状态（true-已登陆，false-未登陆）
     */
    private boolean isUserLogged() {
        return CookieUtil.getCookieValue(clientRequest, ParamConst.AUTHENTICATION) != null;
    }
}
