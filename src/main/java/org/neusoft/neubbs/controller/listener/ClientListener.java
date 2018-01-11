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
import java.util.concurrent.ConcurrentHashMap;

/**
 *  客户端监听器
 *      - 在线访问监控
 *      - 监控 request 和 session
 *          - 使用并发包下 ConcurrentHashMap，维护用户 Session
 *          - ip 不能重复
 *
 *  @author Suvan
 */
public class ClientListener implements HttpSessionListener, ServletRequestListener {

    private ConcurrentHashMap<String, HttpSession> loggedSessionMap = new ConcurrentHashMap<>();

    private HttpServletRequest clientRequest;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("1");
        String ip = clientRequest.getRemoteAddr();
        if (this.isUserLogged()) {
            //login user number +1
            if (loggedSessionMap.get(ip) == null) {
                loggedSessionMap.put(ip, httpSessionEvent.getSession());
                this.updateApplicationLoggedUserNumber(httpSessionEvent.getSession().getServletContext());
            }
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        String ip = clientRequest.getRemoteAddr();

        if (this.isUserLogged()) {
            //login user number -1
            if (loggedSessionMap.get(ip) != null) {
                loggedSessionMap.remove(ip);
                this.updateApplicationLoggedUserNumber(httpSessionEvent.getSession().getServletContext());
            }
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        clientRequest = (HttpServletRequest) servletRequestEvent.getServletRequest();

        //if session no exist, to create new session
        if (loggedSessionMap.get(clientRequest.getRemoteAddr()) == null) {
            clientRequest.getSession(true);
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) { }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /*
     * ***********************************************
     * is method
     * ***********************************************
     */

    /**
     * 判断用户是否已经登陆
     *
     * @return boolean 用户登陆状态（true-已登陆，false-未登陆）
     */
    private boolean isUserLogged() {
        return CookieUtil.getCookieValue(clientRequest, ParamConst.AUTHENTICATION) != null;
    }

    /*
     * ***********************************************
     * update context method
     * ***********************************************
     */

    /**
     * 更新应用已登陆用户数量
     *
     * @param context 全局上下文
     */
    private void updateApplicationLoggedUserNumber(ServletContext context) {
        context.setAttribute(ParamConst.LOGIN_USER, loggedSessionMap.size());
    }
}
