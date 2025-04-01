package com.scut.provider;

import com.scut.RpcApplication;
import com.scut.common.service.UserService;
import com.scut.config.RegistryConfig;
import com.scut.config.RpcConfig;
import com.scut.model.ServiceMetaInfo;
import com.scut.registry.LocalRegistry;
import com.scut.registry.Registry;
import com.scut.registry.RegistryFactory;
import com.scut.server.HttpServer;
import com.scut.server.VertxHttpServer;
import com.scut.server.tcp.VertxTcpServer;


public class Provider {
    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();

        //本地注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try{
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //启动web服务
        //启动HTTP服务
//        HttpServer httpServer = new VertxHttpServer();
//        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

        //启动TCP服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
