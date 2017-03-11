package com.boy.rpc.framework.registry.redis.bean;

/**
 * Created by wesley on 2017-03-09.
 * 封装redis基本属性信息
 */
public class RedisConfig {

    private String redisAddress;
    private String redisPassword;
    private Integer redisPort;

    public String getRedisAddress() {
        return redisAddress;
    }

    public void setRedisAddress(String redisAddress) {
        this.redisAddress = redisAddress;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    @Override
    public String toString() {
        return "Redis{" +
                "redisAddress='" + redisAddress + '\'' +
                ", redisPassword='" + redisPassword + '\'' +
                ", redisPort=" + redisPort +
                '}';
    }
}
