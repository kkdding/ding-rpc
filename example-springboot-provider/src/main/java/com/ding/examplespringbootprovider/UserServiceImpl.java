package com.ding.examplespringbootprovider;

import com.ding.dingrpc.springboot.starter.annotation.RpcService;
import com.ding.example.common.model.User;
import com.ding.example.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * @author: Dding
 * @date: 2024/10/02
 **/
@Service
@RpcService
@Slf4j
public class UserServiceImpl implements UserService {

    public User getUser(User user){
        log.info("服务端UserName: " + user.getName());
        return user;
    }
}
