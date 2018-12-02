package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool pool ;//Jedis 连接池
    private static  Integer maxTotal =Integer.parseInt( PropertiesUtil.getProperty("redis.max.total","20"));//最大的连接数
    private static Integer maxIdle = Integer.parseInt( PropertiesUtil.getProperty("redis.max.idle","20"));//在jedisPool中最大的idel状态的jedis实例的个数
    private static  Integer minIdle = Integer.parseInt( PropertiesUtil.getProperty("redis.min.idle","20"));//在jedisPool中最小的idel状态的jedis实例的个数
    private static Boolean testOnBorrow =Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true")) ;//在borrow一个jedis实例的时候，是否要进行验证的操作，如果赋值为true.则得到的jedis实例肯定是可以用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));
    private static  String  rediIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisport = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    private static void initPoll(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);//连接耗尽时 是否阻塞，false为不阻塞，抛出异常，true为阻塞直到超时。
        pool = new JedisPool(config,rediIp,redisport,2*1000);
    }
    static {
        initPoll();
    }
    public  static Jedis getJedis(){
        return pool.getResource();
    }
    public static void returnResources(Jedis jedis){
        pool.returnResource(jedis);
    }
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("sunkai","sunkai");
        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("praogam is end");
    }
}
