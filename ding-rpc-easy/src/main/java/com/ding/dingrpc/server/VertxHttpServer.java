package com.ding.dingrpc.server;

import io.vertx.core.Vertx;

/**
 * Vertx Http 服务接口
 *
 * @author: Dding
 * @date: 2024/09/16
 **/
public class VertxHttpServer implements HttpServer {
    /**
     * 启动服务
     *
     * @param port
     */
    @Override
    public void start(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 Http 服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        server.requestHandler(request -> {
            // 处理 HTTP 请求
            System.out.println("Received request: " + request.method() + " " + request.uri());

            // 发送 HTTP 响应
            request.response()
                    .putHeader("content-type", "text/plain")
                    .end("hello from Vert.x Http server!");
        });

        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            } else {
                System.out.println("Failed to start server: " + result.cause());
            }
        });
    }
}
