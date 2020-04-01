package cn.worth.redis.utils;

import cn.worth.redis.exception.RedisException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class RedisClient {

    private StringRedisTemplate redisTemplate;

    private ValueOperations<String, String> valueOps;

    private HashOperations<String, String, String> hashOps;

    private ListOperations<String, String> listOps;

    private SetOperations<String, String> setOps;

    private ZSetOperations<String, String> zsetOps;
    /**
     * 前缀，一般为环境前缀
     */
    private String keyPrefix;

    public RedisClient(String keyPrefix, StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.hashOps = redisTemplate.opsForHash();
        this.listOps = redisTemplate.opsForList();
        this.setOps = redisTemplate.opsForSet();
        this.zsetOps = redisTemplate.opsForZSet();
        this.keyPrefix = keyPrefix.endsWith(":") ? keyPrefix : keyPrefix + ":";

        if(log.isDebugEnabled()){
            log.debug("=======init RedisClient with keyPrefix:{}", this.keyPrefix);
        }
    }

    private String genKey(String key) {
        return keyPrefix + key;
    }

    public boolean exists(String key) {
        try{
            return redisTemplate.hasKey(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }

    }

    public void invalidKey(String key) {
        try{
            redisTemplate.delete(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /*
     * 新增方法，方法名尽量与命令保持一直，降低php等其他语言切换的理解成本。
     */

    /*
     * 简单操作=================================================
     */

    /**
     * 设置值。
     *
     * @param key 键。
     * @param value 字符串形式的值。
     * @throws RedisException 如果设置不成功或者发生异常等。
     */
    public void set(String key, String value){
        try{
            valueOps.set(genKey(key), value);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 设置值。
     *
     * @param key 键。
     * @param value 字符串形式的值。
     * @param expire 失效时间(秒)。
     * @throws RedisException 如果设置不成功或者发生异常等。
     */
    public void set(String key, String value, int expire){
        try{
            valueOps.set(genKey(key), value, expire, TimeUnit.SECONDS);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 如果key不存在，设置值。
     *
     * @param key 键。
     * @param value 字符串形式的值。
     * @return 设置成功返回true；未设置返回false；
     * @throws RedisException 如果发生异常等。
     */
    public boolean setnx(String key, String value){
        try{
            return valueOps.setIfAbsent(genKey(key), value);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 如果key不存在，设置值。
     * @param key 键
     * @param value 字符串形式的值。
     * @param expire 失效时间(秒)
     * @return 设置成功返回true；未设置返回false；
     */
    public boolean setnx(String key, String value, int expire){
        try{
            String lua =
                    "local key = KEYS[1]\n" +
                            "local ttl = ARGV[1]\n" +
                            "local value = ARGV[2]\n" +
                            "local lock = redis.call('setnx', key, value)\n" +
                            "if lock == 1 then\n" +
                            "   redis.call('expire', key, ttl)\n" +
                            "end\n" +
                            "return lock";

            List<String> keys = new ArrayList(1);
            keys.add(genKey(key));
            List<String> values = new ArrayList(2);
            values.add(String.valueOf(expire));
            values.add(value);

            Long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                return (Long) jedis.eval(lua, keys, values);
            });
            return result == 1;
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 如果key不存在，设置值。
     * @param key 键
     * @param value 字符串形式的值。
     * @param expire 失效时间
     * @param unit 时间单位
     * @return 设置成功返回true；未设置返回false；
     */
    public boolean setnx(String key, String value, long expire, TimeUnit unit){
        final long rawTimeout = TimeoutUtils.toMillis(expire, unit);
        try{
            String lua =
                    "local key = KEYS[1]\n" +
                            "local ttl = ARGV[1]\n" +
                            "local value = ARGV[2]\n" +
                            "local lock = redis.call('setnx', key, value)\n" +
                            "if lock == 1 then\n" +
                            "   redis.call('pExpire', key, ttl)\n" +
                            "end\n" +
                            "return lock";

            List<String> keys = new ArrayList(1);
            keys.add(genKey(key));
            List<String> values = new ArrayList(2);
            values.add(String.valueOf(rawTimeout));
            values.add(value);

            Long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                return (Long) jedis.eval(lua, keys, values);
            });
            return result == 1;
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 批量设置值。
     *
     * @param map 包含键值对的map。
     * @throws RedisException 如果发生异常等。
     */
    public void mset(Map<String, String> map){
        try{
            Map<String, String> genMap = new HashMap<>();
            map.forEach((k, v) -> genMap.put(genKey(k), v));
            valueOps.multiSet(genMap);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 通过键获取字符串形式的值。
     *
     * @param key 键。
     * @return key对应的值。
     * @throws RedisException 如果发生异常等。
     */
    public String get(String key){
        try{
            key = genKey(key);
            if(log.isDebugEnabled()){
                log.debug("========RedisClient get:{}", key);
            }
            return valueOps.get(key);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 批量通过键获取值的集合。
     *
     * @param keys 键的集合。
     * @return
     *      对应的值的集合。
     * @throws RedisException 如果发生异常等。
     */
    public List<String> mget(List<String> keys){
        try{
            return valueOps.multiGet(keys.stream().map(this::genKey).collect(Collectors.toList()));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 批量通过键获取值的集合。
     *
     * @param keys 键数组。
     * @return
     *      对应的值的集合。
     * @throws RedisException 如果发生异常等。
     */
    public List<String> mget(String... keys){
        try{
            List<String> keyList = Arrays.asList(keys);
            return valueOps.multiGet(keyList.stream().map(this::genKey).collect(Collectors.toList()));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 通过键删除对应的值。
     *
     * @param key 键。
     * @throws RedisException 如果发生异常等。
     */
    public void del(String key){
        try{
            redisTemplate.delete(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }


    /**
     * 通过键对应的值是指定value时，则删除该key。
     *
     * @param key 键。
     * @param value 指定value
     * @throws RedisException 如果发生异常等。
     */
    public void delIfValue(String key, String value){
        try {
            String lua = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

            redisTemplate.execute((RedisCallback) (connection) -> {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                return (long) jedis.eval(lua, Arrays.asList(genKey(key)), Arrays.asList(value));
            });

        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 递增指定键对应的数值。
     * <p>如果不存在key对应的值，那么会先将key的值设置为0，
     * 然后执行incr操作，返回1。
     *
     * @param key 键。
     * @return
     *      递增后key对应的值。
     * @throws RedisException 如果key对应的值不是数值
     * 或者key对应的数值越界或者发生异常等。
     */
    public Long incr(String key){
        try{
            return valueOps.increment(genKey(key), 1);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 将指定键对应的数值增加给定的增量。
     * <p>如果不存在key对应的值，那么会先将key的值设置为0，
     * 然后执行incrBy操作，返回increment。
     *
     * @param key 键。
     * @param increment 增量值。
     * @return
     *      递增后key对应的值。
     * @throws RedisException 如果key对应的值不是数值
     * 或者key对应的数值越界或者发生异常等。
     */
    public long incrBy(String key, long increment){
        try{
            return valueOps.increment(genKey(key), increment);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 将指定键对应的数值增加给定的增量，并在首次incr时给键指定过期时间。
     * <p>如果不存在key对应的值，那么会先将key的值设置为0，
     * 然后执行incrBy操作，返回increment。
     * @param key 键。
     * @param expire 超时时间，秒
     * @return
     */
    public long incrWithTTL(String key, int expire) {
        try {
            String lua = "local increment = redis.call('incr',KEYS[1])\n" +
                    "if increment == 1 then\n" +
                    "    redis.call('expire',KEYS[1], ARGV[1])\n" +
                    "end\n" +
                    "return increment";

            long result = (long) redisTemplate.execute((RedisCallback) (connection) -> {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                return (long) jedis.eval(lua, Arrays.asList(genKey(key)), Arrays.asList(String.valueOf(expire)));
            });
            return result;
        }catch (Throwable e){
            throw new RedisException(e);
        }

    }

    /**
     * 将指定键对应的数值增加给定的增量，并指定key的过期时间点。
     * <p>如果不存在key对应的值，那么会先将key的值设置为0，
     * 然后执行incrBy操作，返回increment。
     * @param key 键。
     * @param unixTimeInMillis 在指定的超时时间点上超时，毫秒
     * @return
     */
    public long incrAt(String key, long unixTimeInMillis) {
        try {
            String lua = "local increment = redis.call('incr',KEYS[1])\n" +
                    "redis.call('pExpireAt',KEYS[1], ARGV[1])\n" +
                    "return increment";

            long result = (long) redisTemplate.execute((RedisCallback) (connection) -> {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                return (long) jedis.eval(lua, Arrays.asList(genKey(key)), Arrays.asList(String.valueOf(unixTimeInMillis)));
            });

        return result;
        }catch (Throwable e){
            throw new RedisException(e);
        }

    }

    /**
     * 给指定的键设置超时时间。
     *
     * @param key 键。
     * @param seconds 超时时间(单位:秒)。
     * @return
     *      如果成功设置超时，返回true；
     *      如果key不存在或者未成功设置超时，返回false。
     */
    public boolean expire(String key, int seconds){
        try{
            return redisTemplate.expire(genKey(key), seconds, TimeUnit.SECONDS);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    public boolean expire(String key, int timeout, TimeUnit unit){
        try{
            return redisTemplate.expire(genKey(key), timeout, unit);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 给指定的键设置超时时间，并指定key的过期时间点。
     *
     * @param key 键。
     * @param date 固定的超时时间点。
     * @return
     *      如果成功设置超时，返回true；
     *      如果key不存在或者未成功设置超时，返回false。
     */
    public boolean expireAt(String key, Date date){
        try{
            return redisTemplate.expireAt(genKey(key), date);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }


    /*
     * 哈希表相关操作====================================
     */

    /**
     * 为键对应的哈希表设置一个字段及对应的值。
     *
     * @param key 哈希表在redis中的key。
     * @param field 哈希表中的字段。
     * @param value field对应的字符串形式的值。
     * @return
     *      如果field是哈希表中的一个新字段，返回1；
     *      如果field已经在哈希表中，值被更新，返回0；
     * @throws RedisException 如果发生异常等。
     */
    public void hset(String key, String field, String value){
        try{
            hashOps.put(genKey(key), field, value);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 为键对应的哈希表设置多个字段及对应的值。
     *
     * @param key 哈希表在redis中的key。
     * @param map 多个字段及对应值组成的哈希表。
     * @throws RedisException 如果发生异常等。
     */
    public void hmset(String key, Map<String, String> map){
        try{
            hashOps.putAll(genKey(key), map);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 从键对应的哈希表中获取给定字段对应的值。
     *
     * @param key 哈希表在redis中的key。
     * @param field 哈希表中的字段。
     * @return
     *      哈希表中field对应的字符串形式的值。
     * @throws RedisException 如果发生异常等。
     */
    public String hget(String key, String field){
        try{
            return hashOps.get(genKey(key), field);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 从键对应的哈希表中获取给定一批字段对应的值。
     *
     * @param key 哈希表在redis中的key。
     * @param fields 哈希表中的一批字段。
     * @return
     *      哈希表中fields对应的字符串形式的值的集合。
     * @throws RedisException 如果发生异常等。
     */
    public List<String> hmget(String key, String ... fields){
        try{
            return hashOps.multiGet(genKey(key), Arrays.asList(fields));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取键对应的哈希表。
     *
     * @param key 哈希表在redis中的key。
     * @return
     *      key对应的哈希表。
     * @throws RedisException 如果发生异常等。
     */
    public Map<String, String> hgetAll(String key){
        try{
            return hashOps.entries(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 从键对应的哈希表中删除给定字段对应的值。
     *
     * @param key 键。
     * @param fields 哈希表中的字段。
     * @return
     *      返回从哈希表中删除字段的数量。
     * @throws RedisException 如果发生异常等。
     */
    public long hdel(String key, String ... fields){
        try{
            return hashOps.delete(genKey(key), fields);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 判断键对应的哈希表中是否存在给定字段及对应的值。
     *
     * @param key 键。
     * @param field 哈希表中的字段。
     * @return
     *      如果存在，返回true；不存在返回false；
     * @throws RedisException 如果发生异常等。
     */
    public boolean hexists(String key, String field){
        try{
            return hashOps.hasKey(genKey(key), field);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应的哈希表中所有字段。
     *
     * @param key 键。
     * @return key对应的哈希表的所有字段的集合。
     * @throws RedisException 如果发生异常等。
     */
    public Set<String> hkeys(String key){
        try{
            return hashOps.keys(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应的哈希表中的字段数量。
     *
     * @param key 键。
     * @return key对应的哈希表的字段数量。
     * @throws RedisException 如果发生异常等。
     */
    public long hlen(String key){
        try{
            return hashOps.size(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /*
     * 列表相关操作=================================
     */

    /**
     * 获取给定键对应的列表的指定下标的字符串形式的值。
     *
     * @param key 键。
     * @param index 下标。
     * @return 如果指定下标存在值，返回该值；否则返回null；
     * @throws RedisException 如果发生异常等。
     */
    public String lindex(String key, long index){
        try{
            return listOps.index(genKey(key), index);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 在给定键对应的列表头部插入一个或多个字符串形式的值。
     *
     * @param key 键。
     * @param values 要插入链表的值。
     * @return 返回链表的长度。
     * @throws RedisException 如果发生异常等。
     */
    public long lpush(String key, String... values){
        try{
            return listOps.leftPushAll(genKey(key), values);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应的列表头部的元素值。
     *
     * @param key 键。
     * @return 链表头部的字符串形式的值。
     * @throws RedisException 如果发生异常等。
     */
    public String lpop(String key){
        try{
            return listOps.leftPop(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应列表中的第一个字符串形式的元素，或者阻塞直到有可用元素或者超时。
     *
     * @param key 键。
     * @param timeout 超时时间(秒)。
     * @return
     * 		列表中的第一个元素；如果超时返回null。
     */
    public String blpop(String key, int timeout){
        try{
            return listOps.leftPop(genKey(key), timeout, TimeUnit.SECONDS);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 在给定键对应的列表尾部插入一个或多个字符串形式的值。
     *
     * @param key 键。
     * @param values 要插入链表的值。
     * @return 返回链表的长度。
     * @throws RedisException 如果发生异常等。
     */
    public long rpush(String key, String ... values){
        try{
            return listOps.rightPushAll(genKey(key), values);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应的列表尾部的元素值。
     *
     * @param key 键。
     * @return 链表尾部的字符串形式的值。
     * @throws RedisException 如果发生异常等。
     */
    public String rpop(String key){
        try{
            return listOps.rightPop(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应列表中的最后一个字符串形式的元素，或者阻塞直到有可用元素或者超时。
     *
     * @param key 键。
     * @param timeout 超时时间(秒)。
     * @return
     * 		列表中的第一个元素；如果超时返回null。
     */
    public String brpop(String key, int timeout){
        try{
            return listOps.rightPop(genKey(key), timeout, TimeUnit.SECONDS);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应的列表长度。
     *
     * @param key 键。
     * @return 如果存在列表，返回列表长度；否则返回0；
     * @throws RedisException 如果发生异常等。
     */
    public long llen(String key){
        try{
            return listOps.size(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应的列表的指定范围的字符串形式的元素。
     *
     * @param key 键。
     * @param start 起始范围。
     * @param end 结束返回。
     * @return 指定范围内的元素集合。
     * @throws RedisException 如果发生异常等。
     */
    public List<String> lrange(String key, long start, long end){
        try{
            return listOps.range(genKey(key), start, end);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /*
     * 集合相关操作=================================
     */

    /**
     * 为给定键对应的集合中添加一个或多个字符串形式的元素。
     *
     * @param key 键。
     * @param values 要添加的元素。
     * @return 成功添加的元素数量。
     * @throws RedisException 如果发生异常等。
     */
    public long sadd(String key, String ... values){
        try{
            return setOps.add(genKey(key), values);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 删除给定键对应的集合元素。
     * @param key 键。
     * @param values 要删除的元素。
     * @return  成功删除的元素数量。
     * @throws RedisException 如果发生异常等。
     */
    public long srem(String key, String... values){
        try{
            return setOps.remove(genKey(key), values);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取给定键对应的集合元素。
     *
     * @param key 键。
     * @return 集合中所有元素
     * @throws RedisException 如果发生异常等。
     */
    public Set<String> smembers(String key){
        try{
            return setOps.members(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 判断给定元素是否在给定键对应的集合中。
     *
     * @param key 键。
     * @param value 元素。
     * @return
     *      如果value是key对应集合中的元素，返回true；否则返回false。
     * @throws RedisException 如果发生异常等。
     */
    public boolean sismember(String key, String value){
        try{
            return setOps.isMember(genKey(key), value);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /*
     * 有序集合相关操作=================================
     */

    /**
     * 给指定键对应的有序集合添加元素。
     *
     * @param key 键。
     * @param value 字符串形式的元素。
     * @param score 元素的分数。
     * @return
     *      如果集合中不存在value，成功插入集合，返回true。
     *      如果集合中已存在value，更新集合元素，返回false。
     * @throws RedisException 如果发生异常等。
     */
    public boolean zadd(String key, String value, double score){
        try{
            return zsetOps.add(genKey(key), value, score);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 给指定键对应的有序集合添加元素。
     *
     * @param key 键。
     * @param map 要插入的元素和分数对。
     * @return
     *      成功插入数量。
     * @throws RedisException 如果发生异常等。
     */
    public long zadd(String key, Map<String, Double> map){
        try{
            if(map == null || map.size() == 0){
                return 0;
            }
            Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>(map.size());
            for(String k : map.keySet()){
                DefaultTypedTuple<String> tuple = new DefaultTypedTuple<>(k, map.get(k));
                tuples.add(tuple);
            }
            return zsetOps.add(genKey(key), tuples);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 删除指定键对应的有序集合中的一批元素。
     *
     * @param key 键。
     * @param values 要删除的字符串形式的元素。
     * @return
     *      从集合中删除的元素数量。
     * @throws RedisException 如果发生异常等。
     */
    public long zrem(String key, String ... values){
        try{
            return zsetOps.remove(genKey(key), values);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 获取指定键对应的有序集合中的元素个数。
     *
     * @param key 键。
     * @return 元素个数。
     * @throws RedisException 如果发生异常等。
     */
    public long zcard(String key){
        try{
            return zsetOps.zCard(genKey(key));
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    /**
     * 从指定键对应的有序集合中获取给定下标范围的字符串形式的元素。
     * <p>元素顺序按分数由小到大。
     *
     * @param key 键。
     * @param start 起始下标。(下标从0开始)
     * @param end 结束下标。
     * @return
     *      指定下标范围内的元素集合。
     * @throws RedisException 如果发生异常等。
     */
    public Set<String> zrange(String key, long start, long end){
        try{
            return zsetOps.range(genKey(key), start, end);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    public Set<String> zrangeByScore(String key, long start, long end){
        try{
            return zsetOps.rangeByScore(genKey(key), start, end);
        }catch (Throwable e){
            throw new RedisException(e);
        }
    }

    public long zcount(String key, double min, double max) {
        return zsetOps.count(genKey(key), min, max);
    }

    public void watch(String key) {
        redisTemplate.watch(genKey(key));
    }

    public List<Object>  exec() {
        return redisTemplate.exec();
    }

    public void multi() {
        redisTemplate.multi();
    }

    public void setEnableTransactionSupport(boolean enable) {
        redisTemplate.setEnableTransactionSupport(enable);
    }

    public boolean rename(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(genKey(oldKey), genKey(newKey));
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(genKey(key));
    }

}