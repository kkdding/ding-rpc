package com.ding.dingrpc.server.tcp;

import com.ding.dingrpc.server.VertxHttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author: Dding
 * @date: 2024/09/23
 **/
@Slf4j
public class VertxTcpServer extends VertxHttpServer {

    private byte[] handleRequest(byte[] requestData) {
        // 在这里编写处理请求的逻辑，根据 requestData 构造响应数据并返回
        System.out.println("Received request: " + Arrays.toString(requestData));
        // 这里只是一个示例，实际逻辑需要根据具体的业务需求来实现
        return "Hello, client!".getBytes();
    }

    /**
     * 启动服务
     *
     * @param port
     */
    @Override
    public void start(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 1.测试处理请求
//        server.connectHandler(socket -> {
//            // 处理连接
//            socket.handler(buffer -> {
//                String testMessage = "Hello, server!Hello, server!Hello, server!Hello, server!";
//                int messageLength = testMessage.getBytes().length;
//                if (buffer.getBytes().length < messageLength) {
//                    log.warn("半包, length = {}", buffer.getBytes().length);
//                    return;
//                }
//                if (buffer.getBytes().length > messageLength) {
//                    log.warn("粘包, length = {}", buffer.getBytes().length);
//                    return;
//                }
//                String str = new String(buffer.getBytes(0, messageLength));
//                System.out.println(str);
//                if (testMessage.equals(str)) {
//                    log.info("good");
//                }
//                // 处理接收到的字节数组
//                byte[] requestData = buffer.getBytes();
//                // 在这里进行自定义的字节数组处理逻辑，比如解析请求、调用服务、构造响应等
//                byte[] responseData = handleRequest(requestData);
//                // 发送响应
//                socket.write(Buffer.buffer(responseData));
//            });
//        });

        // 2.测试处理请求 -> 解决粘包半包问题
//        server.connectHandler(socket -> {
//            String testMessage = "Hello, server!Hello, server!Hello, server!Hello, server!";
//            int messageLength = testMessage.getBytes().length;
//
//            // 构造parser
//            RecordParser parser = RecordParser.newFixed(messageLength);
//            parser.setOutput(buffer -> {
//                String str = new String(buffer.getBytes());
//                log.info("str: {}", str);
//                if (testMessage.equals(str)) {
//                    log.info("str-good");
//                }
//            });
//            socket.handler(parser);
//        });

        // 3.测试处理请求 -> 解决粘包半包问题 -> 消息长度可变
//        server.connectHandler(socket -> {
//            // 构造
//            RecordParser parser = RecordParser.newFixed(8);
//            parser.setOutput(new Handler<Buffer>() {
//                // 初始化
//                int size = -1;
//                // 一次完整的读取（头 + 体）
//                Buffer resultBuffer = Buffer.buffer();
//
//                @Override
//                public void handle(Buffer buffer) {
//                    if (size == -1) {
//                        // 读取消息体长度
//                        size = buffer.getInt(4);
//                        parser.fixedSizeMode(size);
//                        // 写入头信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                    } else {
//                        // 写入体信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                        System.out.println(resultBuffer.toString());
//                        // 重置一轮
//                        parser.fixedSizeMode(8);
//                        size = -1;
//                        resultBuffer = Buffer.buffer();
//                    }
//                }
//            });
//            socket.handler(parser);
//        });


        // 处理请求
        server.connectHandler(new TcpServerHandler());

        // 启动 TCP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port " + port);
            } else {
                System.err.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().start(50008);
    }
}
