package org.neusoft.neubbs.controller.filter;

import org.neusoft.neubbs.utils.PublicParamsUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
    public void init(FilterConfig config) throws ServletException { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //set thread local param
        PublicParamsUtil.setRequest(request);
        PublicParamsUtil.setResponse(response);

        //pass filter, http to the chain
        chain.doFilter(request, response);

        //clear thread local param information
        PublicParamsUtil.clearAllInfo();
    }
}
