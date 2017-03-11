package com.boy.rpc.framework.sample.client;

import com.boy.rpc.framework.client.BoyRpcProxy;
import com.boy.rpc.framework.sample.api.HelloRedisService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wesley on 2017-03-10.
 */
public class RedisHelloClient {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-by-redis.xml");
        BoyRpcProxy rpcProxy = context.getBean(BoyRpcProxy.class);

        HelloRedisService helloService = rpcProxy.create(HelloRedisService.class);
        String result = helloService.sayHello("World");
        System.out.println(result);

        System.exit(0);
    }
}
