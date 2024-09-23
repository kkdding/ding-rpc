package com.ding.dingrpc.registry;

import com.ding.dingrpc.config.RegistryConfig;
import com.ding.dingrpc.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 注册中心测试
 */
@Slf4j
public class RegistryTest {

    final Registry registry = new EtcdRegistry();

    @Before
    public void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void register() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);
        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        registry.register(serviceMetaInfo);
        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("2.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);
    }

    @Test
    public void unRegister() throws ExecutionException, InterruptedException {
        log.info("进入unRegister测试方法");
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.unRegister(serviceMetaInfo);
        log.info("结束unRegister测试方法");
    }

    @Test
    public void serviceDiscovery() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
        Assert.assertNotNull(serviceMetaInfoList);
        System.out.println("第一次读取: " + serviceMetaInfoList);
        List<ServiceMetaInfo> serviceMetaInfoListSecond = registry.serviceDiscovery(serviceKey);
        System.out.println("第二次读取: " + serviceMetaInfoListSecond);
        unRegister();
        Thread.sleep(1000);
        List<ServiceMetaInfo> serviceMetaInfoListThird = registry.serviceDiscovery(serviceKey);
        System.out.println("第三次读取: " + serviceMetaInfoListThird);
    }

    @Test
    public void serviceDiscoveryRealProvider() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("com.ding.example.common.service.UserService");
        serviceMetaInfo.setServiceVersion("1.0");
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
        Assert.assertNotNull(serviceMetaInfoList);
        System.out.println("第一次读取: " + serviceMetaInfoList);
        List<ServiceMetaInfo> serviceMetaInfoListSecond = registry.serviceDiscovery(serviceKey);
        System.out.println("第二次读取: " + serviceMetaInfoListSecond);
        Thread.sleep(10000);
        List<ServiceMetaInfo> serviceMetaInfoListThird = registry.serviceDiscovery(serviceKey);
        System.out.println("第三次读取: " + serviceMetaInfoListThird);
    }

    @Test
    public void heartBeat() throws Exception {
        // init 方法中已经执行心跳检测了
        register();
        // 阻塞 3 分钟
        Thread.sleep(180 * 1000L);
    }
}