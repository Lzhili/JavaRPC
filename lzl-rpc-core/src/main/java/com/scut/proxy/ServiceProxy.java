package com.scut.proxy;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.scut.RpcApplication;
import com.scut.config.RpcConfig;
import com.scut.constant.ProtocolConstant;
import com.scut.constant.RpcConstant;
import com.scut.loadbalancer.LoadBalancer;
import com.scut.loadbalancer.LoadBalancerFactory;
import com.scut.model.RpcRequest;
import com.scut.model.RpcResponse;
import com.scut.model.ServiceMetaInfo;
import com.scut.protocol.*;
import com.scut.registry.Registry;
import com.scut.registry.RegistryFactory;
import com.scut.retry.RetryStrategy;
import com.scut.retry.RetryStrategyFactory;
import com.scut.serializer.SerializerFactory;
import com.scut.serializer.impl.JDKSerializer;
import com.scut.serializer.Serializer;
import com.scut.server.tcp.VertxTcpClient;
import com.scut.tolerant.TolerantStrategy;
import com.scut.tolerant.TolerantStrategyFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理（JDK 动态代理）
 * 当用户调用某个接口的方法时，会改为调用 invoke 方法。在 invoke 方法中，我们可以获取到要调用的方法信息、传入的参数列表等，
 * 这不就是我们服务提供者需要的参数么?用这些参数来构造请求对象就可以完成调用了,
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            //从注册中心获取服务提供者的请求地址，服务发现
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂时没有可用的服务提供者");
            }

            //负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            //将调用方法名（请求路径）作为负载均衡器的参数
            Map<String, Object> requestParams = Map.of("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            System.out.println("负载均衡选择的服务器：" + selectedServiceMetaInfo.getServiceAddress());

//            // 发送http请求
//            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }

            // 发送 TCP 请求
            //使用重试机制
            RpcResponse rpcResponse;
            try{
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() ->
                        VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
                );
            }catch (Exception e){
                // 容错机制
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                rpcResponse = tolerantStrategy.doTolerant(null, e);
            }

            return rpcResponse.getData();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("调用失败");
        }
    }
}
