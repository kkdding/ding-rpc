package com.ding.dingrpc.loadbalancer;

/**
 * 负载均衡器键名常量
 * @author: Dding
 * @date: 2024/09/27
 **/
public interface LoadBalancerKeys {
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机
     */
    String RANDOM = "random";

    /**
     * 一致性哈希
     */
    String CONSISTENT_HASH = "consistentHash";
}
