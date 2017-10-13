package org.neusoft.neubbs.controller.filter;

import org.neusoft.neubbs.constant.account.TokenInfo;
import org.neusoft.neubbs.util.CookieUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  添加 Resposne Header Token 过滤器（访问 /api/* 路径后的添加请求）
 */
public class AddResponseHeaderTokenFilter implements Filter {

    /**
     * 执行过滤器
     * @param req
     * @param resp
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        //访问api，即创建 session(用于统计在线人数)
        request.getSession(true);

        //response Header 添加 Authorization 参数
        if(response.getHeader(TokenInfo.AUTHENTICATION) == null){
            //读取本地 Cookie
            String authorization = CookieUtils.getCookieValue(request, TokenInfo.AUTHENTICATION);//Cookie 取出，没有则为 null
            if(authorization != null){
                response.addHeader(TokenInfo.AUTHENTICATION, authorization);

            }

        }

        //放行
        chain.doFilter(request,response);
    }


    /**
     * 初始化
     * @param config
     * @throws ServletException
     */
    public void init(FilterConfig config) throws ServletException {}

    /**
     * 销毁
     */
    public void destroy(){}
}