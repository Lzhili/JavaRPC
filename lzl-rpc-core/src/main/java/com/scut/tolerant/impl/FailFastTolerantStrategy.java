package com.scut.tolerant.impl;

import com.scut.model.RpcResponse;
import com.scut.tolerant.TolerantStrategy;

import java.util.Map;

/**
 * 快速失败 - 容错策略（立刻通知外层调用方）
 */
public class FailFastTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("fail-fast, 服务报错", e);
    }
}

