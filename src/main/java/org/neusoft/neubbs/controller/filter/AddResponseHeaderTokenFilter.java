package org.neusoft.neubbs.controller.filter;

import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.util.CookieUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  在 Response 的 Header 添加 Authentication: token（获取本地Cookie）
 */
public class AddResponseHeaderTokenFilter implements Filter {
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
        String authorization = CookieUtils.getCookieValue(request, TokenInfo.AUTHENTICATION);
        if(authorization != null){
            response.addHeader(TokenInfo.AUTHENTICATION, authorization);
        }else{
            response.addHeader(TokenInfo.AUTHENTICATION, null);
        }

        chain.doFilter(request,response);//放行
    }


    /**
     * 销毁
     */
    public void destroy(){

    }
}