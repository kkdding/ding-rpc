package com.ding.dingrpc.fault.retry;

import com.ding.dingrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 * @author: Dding
 * @date: 2024/09/28
 **/
public class NoRetryStrategy implements RetryStrategy{
    /**
     * 执行重试
     *
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
