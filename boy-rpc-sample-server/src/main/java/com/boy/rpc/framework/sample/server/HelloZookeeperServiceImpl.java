package com.boy.rpc.framework.sample.server;

import com.boy.rpc.framework.sample.api.HelloZookeeperService;
import com.boy.rpc.framework.server.BoyRpcService;

/**
 * Created by wesley on 2017-03-10.
 */
@BoyRpcService(HelloZookeeperService.class)
public class HelloZookeeperServiceImpl implements HelloZookeeperService {
    @Override
    public String sayHello(String str) {
        return "zookeeper say:"+str+",Hello!";
    }
}
