package com.ding.examplespringbootconsumer;

import com.ding.dingrpc.springboot.starter.annotation.RpcReference;
import com.ding.example.common.model.User;
import com.ding.example.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: Dding
 * @date: 2024/10/02
 **/
@Service
@Slf4j
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void testUser(){
        User user = new User();
        user.setName("ding");
        User newUser = userService.getUser(user);
        log.info("客户端UserName: " + newUser.getName());
    }

}
