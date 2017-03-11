package com.boy.rpc.framework.registry.redis.client;

import com.boy.rpc.framework.registry.redis.Constant;
import com.boy.rpc.framework.registry.redis.bean.RedisConfig;
import com.boy.rpc.framework.common.utils.SerializationUtil;
import com.boy.rpc.framework.registry.redis.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by wesley on 2017-03-10.
 * redis client
 */
public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private static JedisPool jedisPool = null;

    public RedisClient(RedisConfig redisConfig) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Constant.REDIS_MAX_IDLE);
        config.setMaxWaitMillis(Constant.REDIS_MAX_WAIT);
        jedisPool = new JedisPool(config, redisConfig.getRedisAddress(), redisConfig.getRedisPort(), Constant.REDIS_CONNECTION_TIMEOUT, redisConfig.getRedisPassword());
        logger.debug("RedisClient instantiation!");
    }

    /**
     * 获取redis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        logger.debug("RedisClient getJedis start!");
        Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            }
        } catch (Exception e) {
            logger.error("get redis fail"+e);
            e.printStackTrace();
        }
        logger.debug("RedisClient getJedis end!");
        return jedis;
    }

    /**
     * 释放jedis资源
     */
    public static void releaseJedis(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 保存对象
     *
     * @param key
     * @param value
     */
    public static void setObject(String key, Object value) {
        Jedis jedis = getJedis();
        try {
            //jedis.set(key.getBytes(), SerializationUtil.serialize(value));
            jedis.set(key.getBytes(), SerializeUtils.serialize(value));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseJedis(jedis);
        }
    }

    /**
     * 获取保存对象
     *
     * @param key
     * @return
     */
    public static Object getObject(String key) {
        logger.debug("-----getObject----start!!");
        Jedis jedis = getJedis();
        try {
            //此处如果反序列化为Object会空指针异常。看了源码发现是利用Object这个父类对象做的递归判断，由于出现空指针异常不能反序列化成功。故而不用这种方式。源码参看:
            //com.dyuproject.protostuff.runtime.RuntimeSchema.fill(Map<String, Field> fieldMap, Class<?> typeClass);
            //return SerializationUtil.deserialize(jedis.get(key.getBytes()),Object.class);
            return SerializeUtils.unSerialize(jedis.get(key.getBytes()));
        } finally {
            releaseJedis(jedis);
        }
    }

    /**
     * 检查key是否存在
     *
     * @param key
     * @return
     */
    public static boolean existsKey(String key) {
        logger.debug("-----existsKey----start!!");
        Jedis jedis = getJedis();
        try {
            return jedis.exists(key);
        } finally {
            releaseJedis(jedis);
        }
    }
}
