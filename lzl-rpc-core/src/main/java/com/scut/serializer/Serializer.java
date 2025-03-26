package com.scut.serializer;

import java.io.IOException;

/**
 * 序列化器接口
 * 有很多种不同的序列化方式，比如Java原生序列化、JSON、Hessian、Kryo、protobuf等
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}

