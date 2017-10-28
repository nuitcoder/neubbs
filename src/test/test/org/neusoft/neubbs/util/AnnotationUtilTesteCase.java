package test.org.neusoft.neubbs.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neusoft.neubbs.controller.annotation.ApiException;
import org.neusoft.neubbs.controller.api.AccountController;
import org.neusoft.neubbs.controller.exception.FileUploadException;
import org.neusoft.neubbs.utils.AnnotationUtil;

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

    /**
     * 测试 hasMethdoAnnotation()
     */
    @Test
    public void testHasMethodAnnotation(){
        Class[] testClass = {FileUploadException.class, AccountController.class};
        Class specificAnnotationsh = ApiException.class;
        for(Class c : testClass) {
            System.out.println("判断 " + c.getName() + " 是否包含 @" + specificAnnotationsh.getName() + "注解！");
            if (!AnnotationUtil.hasClassAnnotation(c, specificAnnotationsh)) {
                throw new NullPointerException(c.getName() + "不存在指定注解" + specificAnnotationsh.getName());
            }
        }
    }

    @AfterClass
    public static void destroy() {
        System.out.println("*************************** AnnotationUtil 测试完成 ****************************");
    }
}
