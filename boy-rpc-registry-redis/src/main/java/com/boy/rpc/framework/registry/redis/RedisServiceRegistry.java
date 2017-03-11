package com.boy.rpc.framework.registry.redis;

import com.boy.rpc.framework.registry.ServiceRegistry;
import com.boy.rpc.framework.registry.redis.bean.RedisConfig;
import com.boy.rpc.framework.registry.redis.client.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wesley on 2017-03-09.
 * 基于 redis 的服务注册接口实现
 */
public class RedisServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceRegistry.class);

    private RedisClient redisClient = null;

    public RedisServiceRegistry(RedisConfig redisConfig){
        redisClient = new RedisClient(redisConfig);
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        logger.debug("redis register start!");
        if(redisClient.existsKey(serviceName)){
            List<String> oldList = (List<String>) redisClient.getObject(serviceName);
            oldList.add(serviceAddress);
            logger.debug("service exits create service address : {}", oldList);
            redisClient.setObject(serviceName,oldList);
        }else{
            List<String> addressList = new ArrayList<>();
            addressList.add(serviceAddress);
            logger.debug("service not exits create service address : {}", addressList);
            redisClient.setObject(serviceName,addressList);
        }
        logger.debug("redis register end!");
    }

}
