package com.ding.dingrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.ding.dingrpc.RpcApplication;
import com.ding.dingrpc.config.RpcConfig;
import com.ding.dingrpc.constant.RpcConstant;
import com.ding.dingrpc.fault.retry.RetryStrategy;
import com.ding.dingrpc.fault.retry.RetryStrategyFactory;
import com.ding.dingrpc.loadbalancer.LoadBalancer;
import com.ding.dingrpc.loadbalancer.LoadBalancerFactory;
import com.ding.dingrpc.model.RpcRequest;
import com.ding.dingrpc.model.RpcResponse;
import com.ding.dingrpc.model.ServiceMetaInfo;
import com.ding.dingrpc.registry.Registry;
import com.ding.dingrpc.registry.RegistryFactory;
import com.ding.dingrpc.serializer.Serializer;
import com.ding.dingrpc.serializer.SerializerFactory;
import com.ding.dingrpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务代理（JDK 动态代理）
 *
 * @author: Dding
 * @date: 2024/09/17
 **/
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
//        try {
//            // 序列化
//            byte[] bodyBytes = serializer.serialize(rpcRequest);
//            // 构造请求地址
//            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
//            String url = "http://" +
//                    rpcConfig.getServerHost() +
//                    ":" +
//                    rpcConfig.getServerPort();
//            // 发送请求
//            try (HttpResponse httpResponse = HttpRequest.post(url)
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            // 序列化
//            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
//            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

            // 发送 HTTP 请求
//            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }

            // 发送 TCP 请求 TODO: bugs need to be fixed
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo));
            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}
