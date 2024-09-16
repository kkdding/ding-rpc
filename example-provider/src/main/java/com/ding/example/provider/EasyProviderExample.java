package com.ding.example.provider;

import com.ding.dingrpc.registry.LocalRegistry;
import com.ding.dingrpc.server.VertxHttpServer;
import com.ding.example.common.service.UserService;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 提供服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(50000);
    }
}