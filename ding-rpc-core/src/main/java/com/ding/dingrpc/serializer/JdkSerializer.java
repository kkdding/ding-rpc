package com.ding.dingrpc.serializer;

import java.io.*;

/**
 * @author: Dding
 * @date: 2024/09/17
 **/
public class JdkSerializer implements Serializer {
    /**
     * 序列化
     *
     * @param obj
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        try {
            return (T) objectInputStream.readObject();
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }finally {
            objectInputStream.close();
        }
    }
}
