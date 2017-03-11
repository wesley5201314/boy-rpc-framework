package com.boy.rpc.framework.registry;

/**
 * Created by wesley on 2017-03-10.
 * 服务发现接口
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名称查找服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    String discover(String serviceName);
}
