package org.neusoft.neubbs.controller.intercepter;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.login.RedisInfo;
import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.util.AnnotationUtils;
import org.neusoft.neubbs.util.JsonUtils;
import org.neusoft.neubbs.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Login Token 拦截器
 *
 * @Author Suvan
 * @Date 2017-09-28-20:35
 */
public class LoginTokenInterceptor implements HandlerInterceptor{

    @Autowired
    IRedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {//handler（动态代理对象）


        //声明@LoginAuthroization，未通过验证
        if(!statementLoginAuthroization(response,handler)){
            return false;
        }

        return true;//拦截器放行
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
            //存在注解

            //获取验证参数
            String authroization = response.getHeader(LoginInfo.AUTHORIZATION);
            if(authroization != null){
                //解密JWT，获取tokenname
                String tokenName = TokenUtils.verifyToken(authroization, TokenInfo.SECRET_KEY);
                if(tokenName == null){
                    outFailJSONMessage(response, TokenInfo.TOKEN_ALREADEXPIRE);
                    return false;
                }

                //判断redis是否存在相应token
                String rediskey =  redisService.getValueByKey(tokenName);
                if(rediskey == null){
                    outFailJSONMessage(response, RedisInfo.REDIS_USER_LOGINSTATE_ALREADYEXPIRE);
                    return false;
                }


                //通过登录验证，可以执行api
                return true;

            }else{
                //无权限操作
                outFailJSONMessage(response,LoginInfo.NO_VISITAUTHORIZATION);

                return false;//未通过验证
            }
        }

        return true;
    }

    /**
     * 直接输出失败的JSON提示信息
     *
     * @param response
     * @param failMessage
     * @throws JsonProcessingException
     * @throws IOException
     */
    public void outFailJSONMessage(HttpServletResponse response,String failMessage) throws JsonProcessingException,IOException{
        ResponseJsonDTO loginJson = new ResponseJsonDTO();
            loginJson.put(AjaxRequestStatus.FAIL,failMessage);

        String json = JsonUtils.getObjectJSONString(loginJson);

        PrintWriter writer = response.getWriter();
            writer.print(json);
            writer.flush();
            writer.close();
    }
}
