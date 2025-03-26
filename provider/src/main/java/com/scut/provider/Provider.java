package com.scut.provider;

import com.scut.RpcApplication;
import com.scut.common.service.UserService;
import com.scut.registry.LocalRegistry;
import com.scut.server.HttpServer;
import com.scut.server.VertxHttpServer;


public class Provider {
    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();

        //本地注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
