package test.org.neusoft.neubbs.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.controller.annotation.ApiException;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.api.AccountController;
import org.neusoft.neubbs.controller.exception.FileUploadErrorException;
import org.neusoft.neubbs.controller.interceptor.ApiInterceptor;
import org.neusoft.neubbs.utils.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 测试 AnnotationUtil 工具类
 *
 * @author Suvan
 */
@RunWith(JUnit4.class)
public class AnnotationUtilTesteCase {

    @BeforeClass
    public static void init() {
        System.out.println("*************************** 开始测试 AnnotationUitl ****************************");
    }

    @AfterClass
    public static void destroy() {
        System.out.println("*************************** AnnotationUtil 测试完成 ****************************");
    }

    /**
     * （方法级）判断是否存在指定注解
     */
    @Test
    public void testHashMethodAnnotation() throws  Exception {
        //获取 AccountController 类所有 public 方法
        Method[] methods = AccountController.class.getMethods();

        //判断哪些方法存在 LonginAuthorizatin注解
        for (Method m : methods) {
            Annotation annotation = m.getAnnotation(LoginAuthorization.class);
            if (annotation != null) {
                System.out.println(m.getName() + "存在 @LoginAuthorization 注解");
            }
        }
    }


    /**
     * （类级）判断是否包含指定注解
     */
    @Test
    public void testHasClassAnnotation() {
        Class[] classes = {FileUploadErrorException.class, AccountController.class, ApiInterceptor.class};
        Class specificAnnotationsh = ApiException.class;

        for(Class c : classes) {
            System.out.println(c.getSimpleName() + " 类是否包含@" + specificAnnotationsh.getSimpleName() + " 注解："
                                    + AnnotationUtil.hasClassAnnotation(c, specificAnnotationsh));
        }
    }
}
