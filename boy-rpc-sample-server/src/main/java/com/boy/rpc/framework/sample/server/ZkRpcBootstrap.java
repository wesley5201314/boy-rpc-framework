package com.boy.rpc.framework.sample.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ZkRpcBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(ZkRpcBootstrap.class);

    public static void main(String[] args) {
        logger.debug("zk start server");
        new ClassPathXmlApplicationContext("spring-by-zk.xml");
    }
}
