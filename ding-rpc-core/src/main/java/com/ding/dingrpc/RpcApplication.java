package com.ding.dingrpc;

import com.ding.dingrpc.config.RegistryConfig;
import com.ding.dingrpc.config.RpcConfig;
import com.ding.dingrpc.constant.RpcConstant;
import com.ding.dingrpc.registry.Registry;
import com.ding.dingrpc.registry.RegistryFactory;
import com.ding.dingrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC 应用框架
 * 双检锁单例模式实现
 * @author: Dding
 * @date: 2024/09/18
 **/
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 初始化配置, 支持传入自定义配置
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("RPC init success, config = {}", rpcConfig.toString());
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init success, config = {}", registryConfig);
    }

    /**
     * 初始化配置, 默认使用默认配置
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try{
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     * @return
     */
    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if (rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
