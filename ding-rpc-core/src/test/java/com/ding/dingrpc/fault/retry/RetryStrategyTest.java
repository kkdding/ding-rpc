package com.ding.dingrpc.fault.retry;

import com.ding.dingrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 重试策略测试
 *
 * @author: Dding
 * @date: 2024/09/28
 **/
@Slf4j
public class RetryStrategyTest {

    private final RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void testRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
//                return RpcResponse.builder().build();
            });
            log.info("请求结果：{}", rpcResponse);
        } catch (Exception exception) {
            log.error("重试失败原因：{}", exception.getMessage());
        }
    }
}
