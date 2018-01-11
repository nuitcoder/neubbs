package org.neusoft.neubbs.controller.interceptor;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.utils.AnnotationUtil;
import org.neusoft.neubbs.utils.CookieUtil;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  api 拦截器
 *      - 登录验证
 *      - 账户激活验证
 *      - 管理员验证
 *
 *  @author Suvan
 */
public class ApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        //judge @LoginAuthorization, @AccountActivation and @AdminRank
        doLoginAuthorization(request, handler);
        doAccountActivation(request, handler);
        doAdminRank(request, handler);

        //through the api interceptor
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object obj, ModelAndView modelAndView) throws Exception { }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object obj, Exception exception) throws Exception { }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /*
     * ***********************************************
     * authority management method
     * ***********************************************
     */

    /**
     * 执行登录验证
     *      - 判断 api 函数是否标识 @LoginAuthorization
     *      - 判断是否存在 authentication Cookie（不存在表明未登陆, 未登录无权操作）
     *      - 判断 authentication Cookie 是否解密成功（解密失败，表示认证信息已经过期）
     *
     * @param request http请求
     * @param handler 接口方法对象
     */
    private void doLoginAuthorization(HttpServletRequest request, Object handler) throws AccountErrorException {

        if (AnnotationUtil.hasMethodAnnotation(handler, LoginAuthorization.class)) {
            String authentication =  CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
            this.judgeAuthentication(authentication);
        }
    }

    /**
     * 执行账户激活验证
     *      - 判断 api 函数是否标识 @AccountActivation
     *      - 判断是否存在 authentication Cookie（不存在表明未登陆, 未登录无权操作）
     *      - 判断 authentication Cookie 是否解密成功（解密失败，表示认认证信息已经过期）
     *      - 从认证信息内获取用户信息，判断用户激活状态
     *
     * @param request http请求
     * @param handler 方法对象
     */
    private void doAccountActivation(HttpServletRequest request, Object handler) throws AccountErrorException {

        if (AnnotationUtil.hasMethodAnnotation(handler, AccountActivation.class)) {
            String authentication = CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
            UserDO currentUser = this.judgeAuthentication(authentication);

            //judge user state
            if (currentUser.getState() == SetConst.ACCOUNT_NO_ACTIVATED_STATE) {
                throw new AccountErrorException(ApiMessage.NO_ACTIVATE).log(currentUser.getName() + LogWarn.USER_20);
            }
        }
    }

    /**
     * 执行管理员权限验证
     *      - 判断 api 函数是否标识 @AdminRank
     *      - 判断是否存在 authentication Cookie（不存在表明未登陆, 未登录无权操作）
     *      - 判断 authentication Cookie 是否解密成功（解密失败，表示认认证信息已经过期）
     *      - 从认证信息内获取用户信息，判断用户权限
     *
     * @param request http请求
     * @param handler 方法对象
     */
    private void doAdminRank(HttpServletRequest request, Object handler) throws AccountErrorException {

        if (AnnotationUtil.hasMethodAnnotation(handler, AdminRank.class)) {
            String authentication = CookieUtil.getCookieValue(request, ParamConst.AUTHENTICATION);
            UserDO currentUser = this.judgeAuthentication(authentication);

            //judge user rank
            if (!SetConst.RANK_ADMIN.equals(currentUser.getRank())) {
                throw new AccountErrorException(ApiMessage.NO_PERMISSION).log(LogWarn.INTERCEPTOR_03);
            }
        }
    }

    /*
     * ***********************************************
     * judge method
     * ***********************************************
     */

    /**
     * 判断 authentication 认证信息函数
     *      - 若出错则抛出异常
     *
     * @param authentication Cookie内认证信息
     * @return UserDO 用户信息对象实例
     */
    private UserDO judgeAuthentication(String authentication) {
         //judge whether login
         if (authentication == null) {
                this.throwNoPermissionException();
         }

        //judge token validity
        UserDO user = JwtTokenUtil.verifyToken(authentication, SetConst.JWT_TOKEN_SECRET_KEY);
        if (user == null) {
            this.throwTokenExpiredException();
        }

        return user;
    }

    /*
     * ***********************************************
     * throw exception method
     * ***********************************************
     */

    /**
     * 抛出无权限异常
     */
    private void throwNoPermissionException() {
        throw new AccountErrorException(ApiMessage.NO_PERMISSION).log(LogWarn.INTERCEPTOR_02);
    }

    /**
     * 抛出 token 过期异常
     */
    private void throwTokenExpiredException() {
        throw new AccountErrorException(ApiMessage.TOKEN_EXPIRED).log(LogWarn.INTERCEPTOR_01);
    }
}
