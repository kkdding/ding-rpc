package com.ding.example.provider;

import com.ding.example.common.model.User;
import com.ding.example.common.service.UserService;

/**
 * @author: Dding
 * @date: 2024/09/16
 **/
public class UserServiceImpl implements UserService {

    /**
     * 获取用户信息
     *
     * @param user
     * @return
     */
    @Override
    public User getUser(User user) {
        System.out.println("UserName: " + user.getName());
        return user;
    }
}
