package com.scut.consumer;


import com.scut.common.model.User;
import com.scut.common.service.UserService;

/**
 * 简易服务消费者示例
 */
public class EasyConsumer {

    public static void main(String[] args) {
        // todo 需要获取 UserService 的实现类对象
        UserService userService = null;
        User user = new User();
        user.setName("lzl");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}

