package com.alibaba.nacos.test.helloworld;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

/**
 * @author yangzhi
 * @create 2021/2/8
 */
public class Pub {
    public static void main(String[] args) throws NacosException, InterruptedException {
        // 发布的服务名
        String serviceName = "Helloworld.service";
        // 构造一个nacos实例，入参是nacos server的ip和服务端口
        NamingService namingService = NamingFactory.createNamingService("192.168.130.31:8848");
        // 发布自己的服务提供 8888端口
        namingService.registerInstance(serviceName, "127.0.0.1", 8888);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
