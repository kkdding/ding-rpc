package com.ding.dingrpc.fault.tolerant;

import com.ding.dingrpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 * @author: Dding
 * @date: 2024/09/29
 **/
public interface TolerantStrategy {

    /**
     * 执行容错
     * @param context
     * @param exception
     * @return
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception exception);
}
