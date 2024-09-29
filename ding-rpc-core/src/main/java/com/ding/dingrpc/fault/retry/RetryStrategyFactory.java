package com.ding.dingrpc.fault.retry;

import com.ding.dingrpc.spi.SpiLoader;

/**
 * 重试策略工厂（用于获取重试器对象）
 * @author: Dding
 * @date: 2024/09/28
 **/
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试器
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}
