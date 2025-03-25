package com.scut.provider;


import com.scut.server.HttpServer;
import com.scut.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProvider {

    public static void main(String[] args) {
        // 启动 web 服务
        HttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }
}
