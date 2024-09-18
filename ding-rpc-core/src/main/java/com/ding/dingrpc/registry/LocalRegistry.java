package com.ding.dingrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册中心
 * @author: Dding
 * @date: 2024/09/16
 **/
public class LocalRegistry {

    /**
     * 服务注册表
     */
    private static final Map<String, Class<?>> registry = new ConcurrentHashMap<>();

    /**
     * 注册服务
     * @param serviceName
     * @param implClass
     */
    public static void register(String serviceName, Class<?> implClass) {
        registry.put(serviceName, implClass);
    }

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    public static Class<?> getService(String serviceName) {
        return registry.get(serviceName);
    }

    /**
     * 删除服务
     * @param serviceName
     */
    public static void removeService(String serviceName){
        registry.remove(serviceName);
    }
}
