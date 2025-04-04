package com.scut.config;

import com.scut.loadbalancer.LoadBalancerKeys;
import com.scut.retry.RetryStrategyKeys;
import com.scut.serializer.SerializerKeys;
import com.scut.tolerant.TolerantStrategyKeys;
import lombok.Data;

/**
 * RPC 框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "lzl-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 是否开启mock，模拟调用
     */
    private boolean mock = false;

    /**
     * 序列化方式（默认JDK）
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器（默认轮询）
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略（默认不重试）
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略（默认抛出异常，交给外层处理）
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

}
