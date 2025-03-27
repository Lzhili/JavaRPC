package com.scut.consumer;

import com.scut.common.model.User;
import com.scut.common.service.UserService;
import com.scut.config.RpcConfig;
import com.scut.proxy.ServiceProxyFactory;
import com.scut.utils.ConfigUtils;

public class Consumer {
    public static void main(String[] args) {
//        //测试配置文件读取是否正常
//        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
//        System.out.println(rpc);

        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("Hi! lzl");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println(number);
    }
}
