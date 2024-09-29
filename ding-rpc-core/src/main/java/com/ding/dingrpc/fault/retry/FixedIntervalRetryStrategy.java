package com.ding.dingrpc.fault.retry;

import com.ding.dingrpc.model.RpcResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定时间间隔的重试策略
 * @author: Dding
 * @date: 2024/09/28
 **/
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{
    /**
     * 执行重试
     *
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(2L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.hasException()) {
                            log.error("异常信息: {}", attempt.getExceptionCause().getMessage());
                            log.info("请求次数 {}", attempt.getAttemptNumber());
                        } else {
                            log.info("请求次数 {}", attempt.getAttemptNumber());
                        }
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
