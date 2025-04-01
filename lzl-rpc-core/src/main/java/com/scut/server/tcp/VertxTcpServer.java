package com.scut.server.tcp;

import com.scut.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

public class VertxTcpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
//        server.connectHandler(new TcpServerHandler());

        //测试粘包和半包
        server.connectHandler(socket -> {
            String testMessage = "Hello, server!Hello, server!Hello, server!Hello, server!";
            int messageLength = testMessage.getBytes().length;

            // 构造parser
            RecordParser parser = RecordParser.newFixed(messageLength);
            parser.setOutput(new Handler<Buffer>() {

                @Override
                public void handle(Buffer buffer) {
                    String str = new String(buffer.getBytes());
                    System.out.println(str);
                    if (testMessage.equals(str)) {
                        System.out.println("good");
                    }
                }
            });

            socket.handler(parser);
        });

        // 启动 TCP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port " + port);
            } else {
                System.err.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
