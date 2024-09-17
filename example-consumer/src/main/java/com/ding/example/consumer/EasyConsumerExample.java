package com.ding.example.consumer;

import com.ding.example.common.model.User;
import com.ding.example.common.service.UserService;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        // 静态代理
        UserService userService = new UserServiceProxy();
        User user = new User();
        user.setName("ding");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}