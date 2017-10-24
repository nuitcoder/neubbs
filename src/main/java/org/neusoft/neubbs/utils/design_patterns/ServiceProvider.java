package org.neusoft.neubbs.utils.design_patterns;

import java.util.HashMap;
import java.util.Map;

interface Service{

}
interface Provider{
    /**
     * 新建实例
     * @return Service
     */
    Service newService();
}
/**
 * 服务提供者框架
 *
 * @author
 */
public class ServiceProvider {
    private ServiceProvider() { };

    private static final Map<String, Provider> providers = new HashMap<>();
    private static final String DEFAULT_PROVIDER_NAME = "name";

    /**
     * 提供者注册 API
     */
    public static void registerDefaultProvider(Provider p){
        registerProvider(DEFAULT_PROVIDER_NAME, p);
    }
    public static void registerProvider(String name, Provider p){
        providers.put(name, p);
    }

    /**
     * 服务访问 API
     */
    public static Service newInstance(){
        return newInstance(DEFAULT_PROVIDER_NAME);
    }
    public static Service newInstance(String name){
        Provider p = providers.get(name);
        if (p == null) {
            throw new IllegalArgumentException("No provider registered with name：" + name);
        }

        return p.newService();
    }

    /**
     * main
     */
    public static void main(String [] args){
        //测试服务提供者框架（未搞懂）

        Service service = ServiceProvider.newInstance("好好学习");
    }
}
