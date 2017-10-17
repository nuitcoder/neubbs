package org.neusoft.neubbs.controller.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.ajax.ResponseStyleInfo;
import org.neusoft.neubbs.constant.api.AccountInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.util.AnnotationUtils;
import org.neusoft.neubbs.util.CookieUtils;
import org.neusoft.neubbs.util.JWTTokenUtils;
import org.neusoft.neubbs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *  api 拦截器，登录验证 or 管理员权限验证
 */
public class ApiTokenInterceptor implements HandlerInterceptor{

    @Autowired
    IRedisService redisService;

    private static Logger logger = Logger.getLogger(ApiTokenInterceptor.class);

    /**
     * 业务处理器 hander(Controller 内方法) 请求前调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {//handler（动态代理对象）
        //登录验证
        if(!this.doLoginAuthroization(request, response, handler)){
            return false;
        }

        //管理员权限
        if(!this.doAdminRank(request, response, handler)){
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
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView modelAndView) throws Exception {}

    /**
     * 完全处理完请求后调用（可用于清理资源）
     * @param request
     * @param response
     * @param obj
     * @param exception
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception) throws Exception {}

    /**
     * 直接输出失败的JSON提示信息
     *
     * @param response
     * @param failMessage
     * @throws JsonProcessingException
     * @throws IOException
     */
    public void outFailJSONMessage(HttpServletResponse response,String failMessage) throws IOException{
        ResponseJsonDTO responseJson = new ResponseJsonDTO(AjaxRequestStatus.FAIL, failMessage);

        String json = JsonUtils.getJSONStringByObject(responseJson);

        response.setCharacterEncoding(ResponseStyleInfo.CHARACTER_ENCODING);
        response.setContentType(ResponseStyleInfo.CONTENT_TYPE);

        PrintWriter writer = response.getWriter();
            writer.print(json);
            writer.flush();
            writer.close();
    }




    /**
     * @LoginAuthroization 执行登录验证
     *
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    private boolean doLoginAuthroization(HttpServletRequest request, HttpServletResponse response, Object handler) throws  Exception{
        //检查 api 函数是否包含@LoginAuthroizatin
        boolean hasLoginAuthorization = AnnotationUtils.hasMethodAnnotation(handler, LoginAuthorization.class);
        if(hasLoginAuthorization){
            //验证 Authroization 参数
            String authroization =  CookieUtils.getCookieValue(request, AccountInfo.AUTHENTICATION);;//Cookie 内获取
            if(authroization != null){

                //验证客户端 Token 是否过期
                UserDO user = JWTTokenUtils.verifyToken(authroization, SecretInfo.TOKEN_SECRET_KEY);// token 解密
                if(user == null){
                    logger.warn(LogWarnInfo.JWT_TOKEN_ALREAD_EXPIRE);
                    outFailJSONMessage(response, AccountInfo.TOKEN_EXPIRED);
                    return false;
                }
                return true;

            }else{
                //无登录，无权访问 api
                logger.warn(LogWarnInfo.NO_VISIT_AHTORITY_PLEASE_LOGIN);
                outFailJSONMessage(response, AccountInfo.NO_PERMISSION);
                return false;
            }
        }

        //不存在@LoginAuthroization
        return true;
    }


    /**
     * 执行管理员权限验证
     * @param response
     * @param handler
     * @throws Exception
     */
    public Boolean doAdminRank(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        boolean hasAdminRank = AnnotationUtils.hasMethodAnnotation(handler, AdminRank.class);
        if(hasAdminRank){
            String token = CookieUtils.getCookieValue(request,AccountInfo.AUTHENTICATION);
            UserDO user = JWTTokenUtils.verifyToken(token, SecretInfo.TOKEN_SECRET_KEY);
            if(AccountInfo.RANK_ADMIN.equals(user.getRank())){
                return true;//管理员权限
            }else{
                //无管理员权限，无法访问api
                logger.warn(LogWarnInfo.USER_RANK_NO_ENOUGH_NO_ADMIN);
                outFailJSONMessage(response, AccountInfo.NO_PERMISSION);
                return false;
            }
        }

        //不存在@AdminRank
        return true;
    }
}
