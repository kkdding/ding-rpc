package com.ding.example.provider;

import com.ding.dingrpc.RpcApplication;
import com.ding.dingrpc.registry.LocalRegistry;
import com.ding.dingrpc.server.VertxHttpServer;
import com.ding.example.common.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务提供者示例
 * @author: Dding
 * @date: 2024/09/18
 **/
@Slf4j
public class ProviderExample {

    public static void main(String[] args) {

        // RPC框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 提供服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(RpcApplication.getRpcConfig().getServerPort());
    }
}
