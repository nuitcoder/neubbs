package test.org.neusoft.neubbs.util;

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
    /**
     * 测试 指定类，是否存在指定注解
     */
    @Test
    public void testHashClassAnnotation(){
        boolean existResult = AnnotationUtil.hasClassAnnotation(FileUploadException.class, ApiException.class);
        System.out.println("FileUploadException 是否存在 @ApiException：" + existResult);

        existResult = AnnotationUtil.hasClassAnnotation(AccountController.class, ApiException.class);
        System.out.println("AccountController 是否存在 @ApiException：" + existResult);
    }

}
