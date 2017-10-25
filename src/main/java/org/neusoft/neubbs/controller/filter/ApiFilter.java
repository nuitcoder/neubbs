package org.neusoft.neubbs.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  api 过滤器
 *
 *  @author Suvan
 */
public class ApiFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void destroy(){}

    /**
     * 执行过滤器
     *
     * @param req servlet请求
     * @param resp servlet响应
     * @param chain 过滤链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        //访问api，即创建 session(用于统计在线人数)
        request.getSession(true);

        //过滤器放行
        chain.doFilter(request,response);
    }
}