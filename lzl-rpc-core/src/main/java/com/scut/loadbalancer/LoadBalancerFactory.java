package com.scut.loadbalancer;

import com.scut.loadbalancer.impl.RoundRobinLoadBalancer;
import com.scut.spi.SpiLoader;

/**
 * 负载均衡器工厂（工厂模式，用于获取负载均衡器对象）
 */
public class LoadBalancerFactory {

    /**
     * 初始化（加载所有负载均衡器）
     */
    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器（轮询）
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }

}

