package com.scut.retry;

/**
 * 重试策略键名常量
 */
public interface RetryStrategyKeys {
    /**
     * 无重试
     */
    String NO = "no";

    /**
     * 固定间隔重试
     */
    String FIXED_INTERVAL = "fixedInterval";

    /**
     * 指数级回退重试
     */
    String EXPONENTIAL_BACKOFF = "exponentialBackoff";
}
