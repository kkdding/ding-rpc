package com.ding.dingrpc.server;

/**
 * Http 服务接口
 * @author: Dding
 * @date: 2024/09/16
 **/
public interface HttpServer {

    /**
     * 启动服务
     * @param port
     */
    void start(int port);
}
