package com.ding.dingrpc.registry;

import com.ding.dingrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心服务本地缓存
 * @author: Dding
 * @date: 2024/09/22
 **/
public class RegistryServiceCache {

    /**
     * 服务元信息缓存
     */
    private List<ServiceMetaInfo> serviceCache;

    /**
     * 写入缓存
     * @param newServiceMetaInfo
     */
    public void writeCache(List<ServiceMetaInfo> newServiceMetaInfo){
        this.serviceCache = newServiceMetaInfo;
    }

    /**
     * 读取缓存
     * @return
     */
    public List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    public void removeCache(){
        this.serviceCache = null;
    }

}
