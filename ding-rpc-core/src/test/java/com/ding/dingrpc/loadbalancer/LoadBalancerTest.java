package com.ding.dingrpc.loadbalancer;

import com.ding.dingrpc.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡器测试
 * @author: Dding
 * @date: 2024/09/28
 **/
@Slf4j
public class LoadBalancerTest {
    final LoadBalancer loadBalancer = new ConsistentHashLoadBalancer();

    @Test
    public void select() {
        // 请求参数
        Map<String, Object> requestParams1 = new HashMap<>();
        requestParams1.put("methodName", "ding");
        Map<String, Object> requestParams2 = new HashMap<>();
        requestParams2.put("12345", "test");
        // 服务列表
        List<ServiceMetaInfo> serviceMetaInfoList = getServiceMetaInfos();
        // 连续调用 3 次
        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams1, serviceMetaInfoList);
        log.info("第一次信息: {}", serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.select(requestParams1, serviceMetaInfoList);
        log.info("第二次信息: {}", serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.select(requestParams1, serviceMetaInfoList);
        log.info("第三次信息: {}", serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        ServiceMetaInfo serviceMetaInfo_test = loadBalancer.select(requestParams2, serviceMetaInfoList);
        log.info("第四次信息: {}", serviceMetaInfo_test);
        Assert.assertNotNull(serviceMetaInfo_test);
    }

    private static List<ServiceMetaInfo> getServiceMetaInfos() {
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("localhost");
        serviceMetaInfo1.setServicePort(50010);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("ding.com");
        serviceMetaInfo2.setServicePort(50011);
        ServiceMetaInfo serviceMetaInfo3 = new ServiceMetaInfo();
        serviceMetaInfo3.setServiceName("myService");
        serviceMetaInfo3.setServiceVersion("2.0");
        serviceMetaInfo3.setServiceHost("ding.com");
        serviceMetaInfo3.setServicePort(50012);
        return Arrays.asList(serviceMetaInfo1, serviceMetaInfo2, serviceMetaInfo3);
    }
}
