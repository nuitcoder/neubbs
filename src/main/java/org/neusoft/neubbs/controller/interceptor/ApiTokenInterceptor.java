package org.neusoft.neubbs.controller.interceptor;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.constant.api.AccountInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IRedisService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.util.AnnotationUtils;
import org.neusoft.neubbs.util.CookieUtils;
import org.neusoft.neubbs.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  api token 拦截器，登录验证 or 管理员权限验证
 *
 *  @author Suvan
 */
public class ApiTokenInterceptor implements HandlerInterceptor{

    @Autowired
    private IRedisService redisService;

    @Autowired
    private IUserService userService;

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
        if(!doLoginAuthroization(request, response, handler)){
            //未通过验证，拦截
            return false;
        }

        //管理员权限
        if(!doAdminRank(request, response, handler)){
            return false;
        }

        //拦截器放行
        return true;
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
     * @LoginAuthroization 执行登录验证
     *
     * @param response
     * @param handler
     * @return boolean
     * @throws Exception
     */
    private boolean doLoginAuthroization(HttpServletRequest request, HttpServletResponse response, Object handler) throws  Exception{
        //检查 api 函数是否包含@LoginAuthroizatin
        boolean hasLoginAuthorization = AnnotationUtils.hasMethodAnnotation(handler, LoginAuthorization.class);

        if(hasLoginAuthorization){
            //验证 Authroization 参数（Cookie 内日获取）
            String authroization =  CookieUtils.getCookieValue(request, AccountInfo.AUTHENTICATION);;
            if(authroization != null){
                //验证客户端 Token 是否过期（token 能否解密，过期无法解密）
                UserDO user = JwtTokenUtils.verifyToken(authroization, SecretInfo.TOKEN_SECRET_KEY);
                if(user == null){
                    throw new AccountErrorException(AccountInfo.TOKEN_EXPIRED).log(LogWarnInfo.JWT_TOKEN_ALREAD_EXPIRE);
                }

                //自动登录
                return true;

            }else{
                //无登录，无权访问 api
                throw new AccountErrorException(AccountInfo.NO_PERMISSION).log(LogWarnInfo.NO_VISIT_AHTORITY_PLEASE_LOGIN);
            }
        }

        //不存在@LoginAuthroization,直接通过验证
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
            UserDO user = JwtTokenUtils.verifyToken(token, SecretInfo.TOKEN_SECRET_KEY);
            if(AccountInfo.RANK_ADMIN.equals(user.getRank())){
                //拥有管理员权限
                return true;

            }else{
                //无管理员权限，无法访问api
                throw new AccountErrorException(AccountInfo.NO_PERMISSION).log(LogWarnInfo.USER_RANK_NO_ENOUGH_NO_ADMIN);
            }
        }

        //不存在@AdminRank
        return true;
    }
}
