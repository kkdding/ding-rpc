package com.ding.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ding.dingrpc.model.RpcRequest;
import com.ding.dingrpc.model.RpcResponse;
import com.ding.dingrpc.serializer.JdkSerializer;
import com.ding.dingrpc.serializer.Serializer;
import com.ding.example.common.model.User;
import com.ding.example.common.service.UserService;

import java.io.IOException;

/**
 * 静态代理
 * @author: Dding
 * @date: 2024/09/17
 **/
public class UserServiceProxy implements UserService {
    /**
     * 获取用户信息
     *
     * @param user
     * @return
     */
    @Override
    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:50000")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
