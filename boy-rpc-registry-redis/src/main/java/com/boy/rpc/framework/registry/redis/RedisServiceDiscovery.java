package com.boy.rpc.framework.registry.redis;


import com.boy.rpc.framework.registry.ServiceDiscovery;
import com.boy.rpc.framework.registry.redis.bean.RedisConfig;
import com.boy.rpc.framework.registry.redis.client.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by wesley on 2017-03-09.
 * 基于 redis 的服务发现接口实现
 */
public class RedisServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceRegistry.class);

    private RedisClient redisClient = null;

    public RedisServiceDiscovery(RedisConfig redisConfig) {
        redisClient = new RedisClient(redisConfig);
    }

    @Override
    public String discover(String serviceName) {
        String address = null;
        if(redisClient.existsKey(serviceName)){
            List<String> list = (List<String>) redisClient.getObject(serviceName);
            int size = list.size();
            if(size == 1){
                //只有一个地址
                address = list.get(0);
                logger.debug("get only address : {}", address);
            } else {
                // 若存在多个地址，则随机获取一个地址
                address = list.get(ThreadLocalRandom.current().nextInt(size));
                logger.debug("get random address : {}", address);
            }
        }
        return address;
    }
}
