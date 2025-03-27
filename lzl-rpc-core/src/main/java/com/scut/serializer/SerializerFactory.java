package com.scut.serializer;


import com.scut.serializer.impl.HessianSerializer;
import com.scut.serializer.impl.JDKSerializer;
import com.scut.serializer.impl.JsonSerializer;
import com.scut.serializer.impl.KryoSerializer;
import com.scut.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂（用于获取序列化器对象）
 */
public class SerializerFactory {

    /**
     * 序列化映射（用于实现单例）
     * 使用静态代码块，在工厂首次加载时，就会调用 SpiLoader 的 load 方法加载序列化器接口的所有实现类，
     * 之后就可以通过调用 getInstance 方法获取指定的实现类对象了。
     */
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器 JDK
     */
    private static final Serializer DEFAULT_SERIALIZER = new JDKSerializer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
