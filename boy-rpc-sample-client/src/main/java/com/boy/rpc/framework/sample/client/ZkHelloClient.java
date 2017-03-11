package com.boy.rpc.framework.sample.client;

import com.boy.rpc.framework.client.BoyRpcProxy;
import com.boy.rpc.framework.sample.api.HelloZookeeperService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ZkHelloClient {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-by-zk.xml");
        BoyRpcProxy rpcProxy = context.getBean(BoyRpcProxy.class);

        HelloZookeeperService helloService = rpcProxy.create(HelloZookeeperService.class);
        String result = helloService.sayHello("World");
        System.out.println(result);

        System.exit(0);
    }
}
