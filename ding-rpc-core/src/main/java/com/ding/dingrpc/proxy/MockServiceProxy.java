package com.ding.dingrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock 服务代理 (JDK 动态代理)
 *
 * @author: Dding
 * @date: 2024/09/19
 **/
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     * @param proxy
     * @param method
     * @param args
     * @return
     */
    public Object invoke(Object proxy, Method method, Object[] args) {
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultValue(methodReturnType);
    }

    /**
     * 获取默认值
     * @param returnType
     * @return
     */
    private Object getDefaultValue(Class<?> returnType) {
        // 基本类型
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class) {
                return false;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == int.class) {
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            }
        }
        return null;
    }
}
