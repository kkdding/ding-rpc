package com.ding.dingrpc.loadbalancer;

import com.ding.dingrpc.model.ServiceMetaInfo;

import java.util.*;

/**
 * 随机负载均衡器
 *
 * @author: Dding
 * @date: 2024/09/27
 **/
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    /**
     * 选择服务调用节点
     *
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 只有一个服务，无需轮询
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        // 随机算法轮询
        return serviceMetaInfoList.get(random.nextInt(size));
    }

}
