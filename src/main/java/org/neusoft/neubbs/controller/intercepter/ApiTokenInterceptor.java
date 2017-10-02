package org.neusoft.neubbs.controller.intercepter;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.login.LoginInfo;
import org.neusoft.neubbs.constant.db.RedisInfo;
import org.neusoft.neubbs.constant.login.TokenInfo;
import org.neusoft.neubbs.constant.secret.JWTTokenSecret;
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
 * Login Token 拦截器，登录验证
 */
public class ApiTokenInterceptor implements HandlerInterceptor{

    @Autowired
    IRedisService redisService;

    /**
     * 业务处理器 hander(Controller 内方法) 处理方法前调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {//handler（动态代理对象）
        //检测登录权限
        if(!this.checkLoginAuthroization(response,handler)){
            //未通过登录检查
            return false;
        }

        return true;//拦截器放行
    }

    /**
     *处理完方法，return 后，返回视图前，调用
     * @param request
     * @param response
     * @param obj
     * @param modelAndView
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView modelAndView) throws Exception {}

    /**
     *完全处理完请求后调用（可用于清理资源）
     * @param request
     * @param response
     * @param obj
     * @param exception
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception) throws Exception {}

    /**
     * @LoginAuthroization 登录权限验证
     *
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    private boolean checkLoginAuthroization(HttpServletResponse response, Object handler) throws  Exception{
        boolean hasLoginAuthorization = AnnotationUtils.hasMethodAnnotation(handler,LoginAuthorization.class);
        if(hasLoginAuthorization){
            //方法存在@LoginAuthorization
            //获取验证参数
            String authroization = response.getHeader(TokenInfo.AUTHENTICATION);
            if(authroization != null){

                //解密客户端的 JWT ，判断是否过期
                String tokenName = TokenUtils.verifyToken(authroization, JWTTokenSecret.SECRET_KEY);
                if(tokenName == null){
                    //客户端token过期
                    redisService.removeByKey(tokenName); //清除Redis缓存

                    //删除MySQL的forum_token表相应记录

                    outFailJSONMessage(response, TokenInfo.CLIENT_TOKEN_ALREAD_EXPIRE);//输出token过期信息
                    return false;
                }

                //判断redis是否存在相应token，判断判断服务端Redis内用户登录状态是否过期
                String rediskey =  redisService.getValueByKey(tokenName);
                if(rediskey == null){
                    outFailJSONMessage(response, RedisInfo.USER_LOGINSTATE_ALREADYEXPIRE);//用户信息过期
                    return false;
                }

                //通过登录验证，可以执行api
                return true;
            }else{
                //未发现token，无权操作api
                outFailJSONMessage(response, LoginInfo.NO_VISITAUTHORIZATION);
                return false;
            }
        }

        return true;//无需登录验证,直接放行
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

        response.setHeader("ContentType", "text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        PrintWriter writer = response.getWriter();
            writer.print(json);
            writer.flush();
            writer.close();
    }
}
