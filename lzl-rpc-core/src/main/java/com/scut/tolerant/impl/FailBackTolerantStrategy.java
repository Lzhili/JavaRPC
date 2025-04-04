package com.scut.tolerant.impl;

import com.scut.model.RpcRequest;
import com.scut.proxy.ServiceProxyFactory;
import com.scut.tolerant.TolerantStrategy;
import com.scut.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 降级到其他服务 - 容错策略
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        // 通过context里的信息 获取降级接口
        RpcRequest rpcRequest = (RpcRequest) context.getOrDefault("rpcRequest", null);
        if (rpcRequest == null) {
            log.info("FailBackTolerantStrategy", e);
            throw new Exception(e.getMessage());
        }
        String serviceName = rpcRequest.getServiceName();
        Class<?> serviceClass = Class.forName(serviceName);
        //使用模拟接口
        Object mockProxy = ServiceProxyFactory.getMockProxy(serviceClass);
        Method method = mockProxy.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        Object result = method.invoke(mockProxy, rpcRequest.getArgs());
        // 返回结果
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setData(result);
        rpcResponse.setDataType(method.getReturnType());
        rpcResponse.setMessage("Fail Back Tolerant Strategy");
        log.info("fail-back, 降级到mock", e);
        return rpcResponse;
    }
}


