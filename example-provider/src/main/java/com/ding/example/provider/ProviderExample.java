package com.ding.example.provider;

import com.ding.dingrpc.RpcApplication;
import com.ding.dingrpc.bootstrap.ProviderBootstrap;
import com.ding.dingrpc.config.RegistryConfig;
import com.ding.dingrpc.config.RpcConfig;
import com.ding.dingrpc.model.ServiceMetaInfo;
import com.ding.dingrpc.model.ServiceRegisterInfo;
import com.ding.dingrpc.registry.LocalRegistry;
import com.ding.dingrpc.registry.Registry;
import com.ding.dingrpc.registry.RegistryFactory;
import com.ding.dingrpc.server.tcp.VertxTcpServer;
import com.ding.example.common.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者示例
 * @author: Dding
 * @date: 2024/09/18
 **/
@Slf4j
public class ProviderExample {

    public static void main(String[] args) {
        // 服务注册
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserService.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者启动
        ProviderBootstrap.init(serviceRegisterInfoList);
    }

    public static void mainOld(String[] args) {

        // RPC框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);
        log.info("服务注册成功");

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        }catch (Exception e){
            log.error("注册服务到注册中心失败", e);
            throw new RuntimeException(e);
        }

        // 提供 HTTP 服务
//        VertxHttpServer vertxHttpServer = new VertxHttpServer();
//        vertxHttpServer.start(RpcApplication.getRpcConfig().getServerPort());

        // 提供 TCP 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.start(RpcApplication.getRpcConfig().getServerPort());

    }
}
