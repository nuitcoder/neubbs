package org.neusoft.neubbs.controller.interceptor;

import org.neusoft.neubbs.constant.api.AccountInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.AnnotationUtil;
import org.neusoft.neubbs.utils.CookieUtil;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  api 拦截器
 *      1.登录验证
 *      2.账户激活验证
 *      3.管理员验证
 *
 *  @author Suvan
 */
public class ApiInterceptor implements HandlerInterceptor {

    /**
     * 函数处理器（Controller 函数，执行前调用）
     *
     * @param request http请求
     * @param response http响应
     * @param handler 方法对象
     * @return boolean 返回结果（true-放行，false-拦截）
     * @throws Exception 所有异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                Object handler) throws Exception {
        //登录验证，账户激活，管理员级别
        if (!doLoginAuthorization(request, handler)
                || !doAccountActivation(request, handler)
                || !doAdminRank(request, handler)) {
           return false;
        }

        //是否通过（true-通过，false-拦截，默认是true）
        return true;
    }

    /**
     * 请求处理器（函数 return 后，跳转视图前调用）
     *
     * @param request http请求
     * @param response http响应
     * @param obj 方法对象
     * @param modelAndView 视图对象
     * @throws Exception 所有异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                                Object obj, ModelAndView modelAndView) throws Exception { }

    /**
     * 请求完成成立（MVC 控制器 DispatcherServlet 完全处理完请求后调用，可用于清理资源）
     *
     * @param request http请求
     * @param response http响应
     * @param obj 方法对象
     * @param exception 异常对象（可处理函数所抛出的异常）
     * @throws Exception 所有异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                    Object obj, Exception exception) throws Exception { }


    /**
     * 执行登录验证
     *
     * @param request http请求
     * @param handler 方法对象
     * @return boolean 验证结果
     * @throws Exception 所有异常
     */
    private boolean doLoginAuthorization(HttpServletRequest request, Object handler) throws  Exception {

        /*
         * 验证流程：
         *      1.验证拦截 api 函数，是否包含 @LoginAuthorization
         *      2.获取 Cookie 内 authentication 参数（登陆时，JWT 加密信息） ?
         *      3.解密 authentication（过期无法解密），获取用户信息（UserDO 对象）
         *      4.判断（能获取用户信息，表示已经登录）
         */
        boolean hasLoginAuthorization = AnnotationUtil.hasMethodAnnotation(handler, LoginAuthorization.class);
        if (hasLoginAuthorization) {
            String authentication =  CookieUtil.getCookieValue(request, AccountInfo.AUTHENTICATION);
            if (authentication != null) {
                UserDO user = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
                if (user != null) {
                    //通过验证
                    return true;
                }

                throw new AccountErrorException(AccountInfo.TOKEN_EXPIRED).log(LogWarnInfo.INTERCEPTRO_01);
            } else {
                //无登录，无权访问 api
                throw new AccountErrorException(AccountInfo.NO_PERMISSION).log(LogWarnInfo.INTERCEPTRO_02);
            }
        }

        //直接放行
        return true;
    }

    /**
     * 执行账户激活验证
     *
     * @param request http请求
     * @param handler 方法对象
     * @return boolean 验证结果
     * @throws Exception 所有异常
     */
    private boolean doAccountActivation(HttpServletRequest request, Object handler) throws Exception {
        boolean hasAccountActivation = AnnotationUtil.hasMethodAnnotation(handler, AccountActivation.class);
        if (hasAccountActivation) {
            String authentication = CookieUtil.getCookieValue(request, AccountInfo.AUTHENTICATION);
            if (authentication != null) {
                UserDO user = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
                if (user == null) {
                    //JWT Token 过期，解密失败
                    throw new AccountErrorException(AccountInfo.TOKEN_EXPIRED)
                                .log(LogWarnInfo.ACCOUNT_05);
                }
                if (user.getState() == 1) {
                    //通过验证
                    return true;
                }

                throw new AccountErrorException(AccountInfo.NO_ACTIVATE)
                            .log(user.getName() + LogWarnInfo.ACCOUNT_03);
            }
        }

        return true;
    }

    /**
     * 执行管理员权限验证
     *
     * @param request http请求
     * @param handler 方法对象
     * @return boolean 验证结果
     * @throws Exception 所有异常
     */
    private boolean doAdminRank(HttpServletRequest request, Object handler) throws Exception {
        boolean hasAdminRank = AnnotationUtil.hasMethodAnnotation(handler, AdminRank.class);
        if (hasAdminRank) {
            String authentication = CookieUtil.getCookieValue(request, AccountInfo.AUTHENTICATION);
            if (authentication != null) {
                UserDO user = JwtTokenUtil.verifyToken(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
                if (user == null) {
                    throw new AccountErrorException(AccountInfo.TOKEN_EXPIRED)
                                .log(LogWarnInfo.ACCOUNT_05);
                }
                if (AccountInfo.RANK_ADMIN.equals(user.getRank())) {
                    //通过验证
                    return true;
                }

                throw new AccountErrorException(AccountInfo.NO_PERMISSION).log(LogWarnInfo.INTERCEPTRO_03);
            }
        }

        return true;
    }
}
