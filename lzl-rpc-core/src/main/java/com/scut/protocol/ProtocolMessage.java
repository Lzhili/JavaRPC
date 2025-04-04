package com.scut.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协议消息结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {

    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体（请求或响应对象）
     */
    private T body;

    /**
     * 协议消息头
     */
    @Data
    public static class Header {

        /**
         * 魔数，保证安全性
         * 作用是安全校验，防止服务器处理了非框架发来的乱七八糟的消息(类似 HTTPS 的安全证书)
         * 1 字节
         */
        private byte magic;

        /**
         * 版本号
         * 保证请求和响应的一致性(类似 HTTP 协议有 1.0/2.0 等版本)
         * 1 字节
         */
        private byte version;

        /**
         * 序列化器
         * 来告诉服务端和客户端如何解析数据(类似 HTTP 的 Content-Type 内容类型)
         * 1 字节
         */
        private byte serializer;

        /**
         * 消息类型（请求 / 响应）
         * 标识是请求还是响应?或者是心跳检测等其他用途。(类似 HTTP 有请求头和响应头)
         * 1 字节
         */
        private byte type;

        /**
         * 状态
         * 如果是响应，记录响应的结果(类似 HTTP 的 200 状态代码)
         * 1 字节
         */
        private byte status;

        /**
         * 请求 id
         * 8 字节
         */
        private long requestId;

        /**
         * 消息体长度
         * 4 字节
         */
        private int bodyLength;
    }

}
