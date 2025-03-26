package com.scut.consumer;

import com.scut.config.RpcConfig;
import com.scut.utils.ConfigUtils;

public class Consumer {
    public static void main(String[] args) {
        //测试配置文件读取是否正常
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
