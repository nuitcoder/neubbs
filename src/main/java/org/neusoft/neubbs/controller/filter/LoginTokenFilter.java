package org.neusoft.neubbs.controller.filter;

import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.util.CookieUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  LoginToken 过滤器
 */
public class LoginTokenFilter implements Filter {
    /**
     * 初始化
     *
     * @param config
     * @throws ServletException
     */
    public void init(FilterConfig config) throws ServletException {

    }

    /**
     * 执行过滤器
     *
     * @param req
     * @param resp
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        //response Header 添加 Authorization参数
        String authorization = CookieUtils.getCookieValue(request,LoginInfo.AUTHORIZATION);
        if(authorization != null){
            response.addHeader(LoginInfo.AUTHORIZATION,authorization);
        }else{
            response.addHeader(LoginInfo.AUTHORIZATION,null);
        }

        chain.doFilter(request,response);//放行
    }

    /**
     * 销毁
     */
    public void destroy(){

    }
}
