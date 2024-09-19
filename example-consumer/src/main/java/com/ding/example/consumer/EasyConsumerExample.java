package com.ding.example.consumer;

import com.ding.dingrpc.proxy.ServiceProxyFactory;
import com.ding.example.common.model.User;
import com.ding.example.common.service.UserService;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        // 静态代理
//        UserService userService = new UserServiceProxy();
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("ding");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
            System.out.println(newUser);
        } else {
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println("number -> " + number);
    }
}