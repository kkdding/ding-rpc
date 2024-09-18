package com.ding.example.consumer;

import com.ding.dingrpc.config.RpcConfig;
import com.ding.dingrpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 * @author: Dding
 * @date: 2024/09/18
 **/
public class ConsumerExample {

    public static void main(String[] args) {
        // 测试配置文件读取
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpcConfig);
    }
}
