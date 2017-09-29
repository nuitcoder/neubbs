package org.neusoft.neubbs.controller.intercepter;

import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.util.TokenUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Login Token 拦截器
 *
 * @Author Suvan
 * @Date 2017-09-28-20:35
 */
public class LoginTokenInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        //不是方法级跳过
        if(!(obj instanceof HandlerMethod)){
            return true;
        }

        //是否需要登录授权（是否含有@LoginAuthorization）
        HandlerMethod handlerMethod = (HandlerMethod)obj;
        Method method = handlerMethod.getMethod();
        Annotation annotation = method.getAnnotation(LoginAuthorization.class);
        if(annotation != null){
            String authroization = response.getHeader(LoginInfo.AUTHORIZATION);
            if(authroization != null){
               String username = TokenUtils.verifyToken(authroization, TokenInfo.SECRET_KEY);
                System.out.println(username);
            }
        }

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception) throws Exception {

    }
}
