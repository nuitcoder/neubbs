package org.neusoft.neubbs.utils.design_patterns;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.单例模式的 2 种方式
 * 2.静态初始化（实例只执行 1 次）
 * 3.消除过期的对象引用
 * 4.避免使用终结方法
 * 5.守卫类终结
 *
 * @author Suvan
 */
public class Singleton {

    private static final Map<String, Object> MAP;

    static {
        //只会实例化 1 词
        MAP = new HashMap<String, Object>();
    }

    public static final Singleton ONE = new Singleton();

    private Singleton(){
        //初始化属性（私有只会初始化 1 次），反射可调用私有构造器

        //强化不可实例化（直接抛出错误断言）
        throw new AssertionError();
    }

    public static Singleton getInstance(){
        return ONE;
    }
}

/**
 * 氮元素的枚举类型，实现 Singleton 的最佳方法
 */
enum SingletonTwo{
    TWO;
}

/**
 * 守卫类终结
 */
class Foo{
    private final Object finalizaerGuardian = new Object() {
        @Override
        protected void finalize() throws Throwable {
            //终结外围类 FOO 对象
        }
    };
}
