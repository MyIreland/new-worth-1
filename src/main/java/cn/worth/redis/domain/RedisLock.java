package cn.worth.redis.domain;

import cn.worth.redis.exception.RedisException;
import cn.worth.redis.utils.RedisClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * Redis 锁
 * @author chenxiaoqing
 */
@Slf4j
@Getter
public class RedisLock {
    /**
     * redis 缓存工具
     */
    private RedisClient redisUtils;
    /**
     * 锁的缓存key
     */
    private String lockKey;
    /**
     * 锁超时时间，防止入锁后，无限的执行等待 60s
     */
    private long expireMSec = 60 * 1000;
    /**
     * 锁等待时间，防止线程饥饿 10s
     */
    private int timeoutMSec = 10 * 1000;
    /**
     * 锁等待时间，防止线程饥饿 10s
     */
    private volatile boolean locked = false;
    /**
     * 每次等待循环控制 600 次
     */
    private static final int PER_ACQUIRE_TIME = 100;
    /**
     * 每次循环线程随机等待时间 70ms
     */
    private static final int SLEEP_TIME = 70;
    /**
     * 规范锁key后缀
     */
    private static final String LOCK_SUFFIX = "_lock";

    public RedisLock(RedisClient redisUtils, String lockKey){
        if(!lockKey.endsWith(LOCK_SUFFIX)){
            throw new RedisException("锁Key需要以" + LOCK_SUFFIX + "结尾");
        }
        this.redisUtils = redisUtils;
        this.lockKey = lockKey;
    }

    public RedisLock(RedisClient redisUtils, String lockKey, int timeoutMSec) {
        this(redisUtils, lockKey);
        this.timeoutMSec = timeoutMSec;
    }

    /**
     * jedis 的getSet
     * 设置新值，返回旧值，没有旧值，返回 nil
     * @param key
     * @param value
     * @return
     */
    private String getSet(final String key, final String value) {
        Object obj = null;
        try {
            obj = redisUtils.getRedisTemplate().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] ret = connection.getSet(serializer.serialize(key),
                            serializer.serialize(value));
                    connection.close();
                    return serializer.deserialize(ret);
                }
            });
        } catch (Exception e) {
            log.error("setNX redis error, key : {}", key);
        }
        return obj != null ? (String) obj : null;
    }

    /**
     * 锁住对象
     * @return
     * @throws InterruptedException
     */
    public synchronized boolean lock() throws InterruptedException {
        int timeout = timeoutMSec;
        Random random = new Random();

        while (timeout > 0) {

            long expire = System.currentTimeMillis() + expireMSec + 1;

            String expiresStr = String.valueOf(expire); // 锁到期时间
            if (redisUtils.setnx(lockKey, expiresStr)) {
                // 可以拿到锁 则返回true，没有人进入过锁
                locked = true;
                return locked;
            }
            // 有人已经设置了锁，得到Key里的时间
            String currentTimeStr = redisUtils.get(lockKey);
            if(!StringUtils.isEmpty(currentTimeStr) && Long.parseLong(currentTimeStr) < System.currentTimeMillis()){
                // 判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                // 锁过期 获取上一个锁到期时间，并设置现在的锁到期时间，
                // 只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                String oldTimeStr = this.getSet(lockKey, expiresStr);

                if (oldTimeStr != null && oldTimeStr.equals(currentTimeStr)) {
                    // 防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为相差了很少的时间，所以可以接受
                    // [分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
                    locked = true;
                    return locked;
                }
            }
            timeout -= PER_ACQUIRE_TIME;

            /*
             * 延迟100 毫秒, 这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
             * 只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
             * 使用随机的等待时间可以一定程度上保证公平性,避开高峰
             */
            Thread.sleep(SLEEP_TIME + random.nextInt(SLEEP_TIME));
        }
        return false;
    }

    /**
     * 释放锁
     */
    public synchronized void unlock() {
        if (locked) {
            redisUtils.del(lockKey);
            locked = false;
        }
    }
}
