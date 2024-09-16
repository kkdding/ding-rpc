package com.ding.example.common.service;

import com.ding.example.common.model.User;

/**
 * @author: Dding
 * @date: 2024/09/16
 **/
public interface UserService {

    /**
     * 获取用户信息
     * @return
     */
    User getUser(User user);
}
