package com.ding.dingrpc.config;

import com.ding.dingrpc.loadbalancer.LoadBalancerKeys;
import com.ding.dingrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC 框架配置
 *
 * @author: Dding
 * @date: 2024/09/18
 **/
@Data
public class RpcConfig {

    private boolean mock = false;

    /**
     * 名称
     */
    private String name = "ding-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 50000;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

}
