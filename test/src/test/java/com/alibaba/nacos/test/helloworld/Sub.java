package com.alibaba.nacos.test.helloworld;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;

/**
 * @author yangzhi
 * @create 2021/2/8
 */
public class Sub {

    public static void main(String[] args) throws NacosException, InterruptedException {
        //订阅的服务名
        String serviceName = "helloworld.services";
        //创建一个nacos实例
        NamingService naming = NamingFactory.createNamingService("100.81.0.34:8080");
        //订阅一个服务
        naming.subscribe(serviceName, event -> {
            if (event instanceof NamingEvent) {
                System.out.println("订阅到数据");
                System.out.println(((NamingEvent) event).getInstances());
            }
        });
        System.out.println("订阅完成，准备等数来");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
