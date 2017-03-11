package com.boy.rpc.framework.sample.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wesley on 2017-03-10.
 */
public class RedisRpcBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(RedisRpcBootstrap.class);

    public static void main(String[] args) {
        logger.debug("redis rpc start server");
        new ClassPathXmlApplicationContext("spring-by-redis.xml");
    }
}
