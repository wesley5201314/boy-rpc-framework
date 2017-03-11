package com.boy.rpc.framework.sample.server;

import com.boy.rpc.framework.sample.api.HelloRedisService;
import com.boy.rpc.framework.server.BoyRpcService;

/**
 * Created by wesley on 2017-03-10.
 */
@BoyRpcService(HelloRedisService.class)
public class HelloRedisServiceImpl implements HelloRedisService {
    @Override
    public String sayHello(String str) {
        return "redis say:"+str+",Hello!";
    }
}
