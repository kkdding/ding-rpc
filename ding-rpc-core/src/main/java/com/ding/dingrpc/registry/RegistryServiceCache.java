package com.ding.dingrpc.registry;

import com.ding.dingrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心服务本地缓存
 * @author: Dding
 * @date: 2024/09/22
 **/
public class RegistryServiceCache {

    /**
     * 服务元信息缓存
     */
    private Map<String,List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写入缓存
     * @param serviceKey
     * @param newServiceMetaInfo
     */
    public void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceMetaInfo){
        this.serviceCache.put(serviceKey, newServiceMetaInfo);
    }

    /**
     * 读取缓存
     * @param serviceKey
     * @return
     */
    public List<ServiceMetaInfo> readCache(String serviceKey){
        return this.serviceCache.get(serviceKey);
    }

    /**
     * 删除缓存
     * @param serviceKey
     */
    public void removeCache(String serviceKey){
        this.serviceCache.remove(serviceKey);
    }

}
