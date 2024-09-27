package com.ding.dingrpc.loadbalancer;

import com.ding.dingrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器（消费端使用）
 * @author: Dding
 * @date: 2024/09/27
 **/
public interface LoadBalancer {

    /**
     * 选择服务调用节点
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
