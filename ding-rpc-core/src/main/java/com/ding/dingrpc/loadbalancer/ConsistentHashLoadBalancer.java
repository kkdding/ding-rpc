package com.ding.dingrpc.loadbalancer;

import com.ding.dingrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 * @author: Dding
 * @date: 2024/09/27
 **/
public class ConsistentHashLoadBalancer implements LoadBalancer{

    /**
     * 一致性 Hash 环，存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点个数
     */
    private static final int VIRTUAL_NODE_SIZE = 100;

    /**
     * 选择服务调用节点
     *
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty()){
            return null;
        }

        // 构建虚拟节点环
        for(ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList){
            for(int i = 0; i < VIRTUAL_NODE_SIZE; i++){
                virtualNodes.put(getHash(serviceMetaInfo.getServiceAddress() + "#" + i), serviceMetaInfo);
            }
        }

        // 获取请求的哈希值
        Integer hash = getHash(requestParams);

        // 选择最接近且大于等于调用请求 hash 值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if(entry == null){
            // 如果没有大于等于调用请求 hash 值的虚拟节点，则返回环首部的节点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();

    }

    /**
     * 哈希算法（支持自定义）
     * @param key
     * @return
     */
    private Integer getHash(Object key){
        return key.hashCode();
    }

}
