package com.scut.consumer;


import com.scut.common.model.User;
import com.scut.common.service.UserService;
import com.scut.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 */
public class EasyConsumer {

    public static void main(String[] args) {
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("lzl123");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}

