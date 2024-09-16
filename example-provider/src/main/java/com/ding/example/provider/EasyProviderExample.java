package com.ding.example.provider;

import com.ding.dingrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 提供服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(50000);
    }
}