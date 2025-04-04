package com.scut.tolerant.impl;

import com.scut.model.RpcResponse;
import com.scut.tolerant.TolerantStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理异常 - 容错策略
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("fail-safe, 静默处理异常", e);
        return new RpcResponse();
    }
}
