package com.scut.retry;

import com.scut.model.RpcResponse;
import com.scut.retry.impl.ExponentialBackoffRetryStrategy;
import com.scut.retry.impl.FixedIntervalRetryStrategy;
import com.scut.retry.impl.NoRetryStrategy;
import org.junit.Test;

/**
 * 重试策略测试
 */
public class RetryStrategyTest {

//    RetryStrategy retryStrategy = new NoRetryStrategy(); //不重试
//    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy(); //固定时间
    RetryStrategy retryStrategy = new ExponentialBackoffRetryStrategy(); //指数退避
    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}

