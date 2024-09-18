package com.ding.dingrpc.serializer;

import java.io.IOException;

/**
 * @author: Dding
 * @date: 2024/09/17
 **/
public interface Serializer {

    /**
     * 序列化
     * @param obj
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
