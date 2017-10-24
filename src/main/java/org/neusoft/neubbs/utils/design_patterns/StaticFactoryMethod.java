package org.neusoft.neubbs.utils.design_patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 静态工厂方法
 *
 * @author Suvan
 */
public class StaticFactoryMethod {

    /**
     * 获取 ArrayList 实例
     *
     * @return
     */
    public static <E> ArrayList<E> newArrayListInstance(){
        return new ArrayList<E>();
    }

    /**
     * 获取 HashMap实例
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMapInstance(){
        return new HashMap<K, V>();
    }

    /**
     * main
     */
    public static void main(String [] args){
        Map<String, List<String>> map = StaticFactoryMethod.newHashMapInstance();
        List<String> list = StaticFactoryMethod.newArrayListInstance();
            list.add("你好我是皮皮虾");
            map.put("学习", list);

        System.out.println(map);
    }
}
