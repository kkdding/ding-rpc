package com.ding.dingrpc.fault.tolerant;

import com.ding.dingrpc.model.RpcResponse;

import java.util.Map;

/**
 * 降级到其他服务 - 容错策略
 * @author: Dding
 * @date: 2024/09/29
 **/
public class FailBackTolerantStrategy implements TolerantStrategy{
    /**
     * 执行容错
     *
     * @param context
     * @param exception
     * @return
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception exception) {
        // TODO:获取降级的服务并调用
        return null;
    }
}
