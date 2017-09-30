package org.neusoft.neubbs.controller.intercepter;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.util.AnnotationUtils;
import org.neusoft.neubbs.util.JsonUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Login Token 拦截器
 *
 * @Author Suvan
 * @Date 2017-09-28-20:35
 */
public class LoginTokenInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {//handler（动态代理对象）


        //声明@LoginAuthroization
        if(!statementLoginAuthroization(response,handler)){
            return false;
        }

        return true;//无 @LoginAuthroization 直接放行
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception) throws Exception {

    }

    /**
     * @LoginAuthroization 权限验证
     *
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    private boolean statementLoginAuthroization(HttpServletResponse response, Object handler) throws  Exception{
        //方法体是否声明@LoginAuthorization
        boolean checkLoginAuthorization = AnnotationUtils.checkMethodAnnotation(handler,LoginAuthorization.class);
        if(checkLoginAuthorization){

            //获取验证参数
            String authroization = response.getHeader(LoginInfo.AUTHORIZATION);
            if(authroization != null){
                //判断redis是否存在相应token

                //解密token，获取用户名
                //String username = TokenUtils.verifyToken(authroization, TokenInfo.SECRET_KEY);

                //获取用户数据,返回相应JSON格式

                return true;//通过验证
            }else{
                //无权限操作
                ResponseJsonDTO loginJson = new ResponseJsonDTO();
                loginJson.put(AjaxRequestStatus.FAIL,LoginInfo.NO_VISITAUTHORIZATION);

                String json = JsonUtils.getObjectJSONString(loginJson);

                PrintWriter writer = response.getWriter();
                writer.print(json);
                writer.flush();
                writer.close();

                return false;//未通过验证
            }
        }

        return true;
    }
}
