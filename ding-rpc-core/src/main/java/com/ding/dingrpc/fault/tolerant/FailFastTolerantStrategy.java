package com.ding.dingrpc.fault.tolerant;

import com.ding.dingrpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败 - 容错策略（立刻通知外层调用方）
 * @author: Dding
 * @date: 2024/09/29
 **/
public class FailFastTolerantStrategy implements TolerantStrategy{
    /**
     * 执行容错
     *
     * @param context
     * @param exception
     * @return
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception exception) {
        throw new RuntimeException("服务错误", exception);
    }
}
