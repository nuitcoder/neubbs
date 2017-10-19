package org.neusoft.neubbs.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  添加 resposne Header Token 过滤器（访问 /api/* 路径后的添加请求）
 *
 *  @author Suvan
 */
public class ApiFilter implements Filter {

    /**
     * 执行过滤器
     * @param req
     * @param resp
     * @param chain
     * @throws ServletException
     * @throws IOException
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


    /**
     * 初始化
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {}

    /**
     * 销毁
     */
    @Override
    public void destroy(){}
}