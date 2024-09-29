package com.ding.dingrpc.fault.tolerant;

import com.ding.dingrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理异常 - 容错策略
 * @author: Dding
 * @date: 2024/09/29
 **/
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    /**
     * 执行容错
     *
     * @param context
     * @param exception
     * @return
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception exception) {
        log.warn("静默处理异常", exception);
        return new RpcResponse();
    }
}
