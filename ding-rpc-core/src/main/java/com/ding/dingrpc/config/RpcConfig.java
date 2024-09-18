package com.ding.dingrpc.config;

import lombok.Data;

/**
 * RPC 框架配置
 *
 * @author: Dding
 * @date: 2024/09/18
 **/
@Data
public class RpcConfig {
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

}
