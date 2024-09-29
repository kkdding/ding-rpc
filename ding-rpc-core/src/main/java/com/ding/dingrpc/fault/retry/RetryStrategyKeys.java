package com.ding.dingrpc.fault.retry;

/**
 * @author: Dding
 * @date: 2024/09/28
 **/
public interface RetryStrategyKeys {
    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定时间间隔
     */
    String FIXED_INTERVAL = "fixedInterval";
}
