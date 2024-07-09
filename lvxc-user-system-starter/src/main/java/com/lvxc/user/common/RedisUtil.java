package com.lvxc.user.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static final Charset charset = Charset.forName("UTF-8");

    public Long incr(String key) {
        final byte[] keyBytes = key.getBytes(charset);
        Long result = redisTemplate.execute((RedisCallback<Long>) connection -> connection.incr(keyBytes));
        return result;
    }


    public boolean put(String key, Object obj) {
        final byte[] keyBytes = key.getBytes(charset);
        String json = JSONObject.toJSONString(obj);
        final byte[] valueBytes = json.getBytes(charset);

        boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(keyBytes, valueBytes));
        return result;
    }


    public boolean putNx(String key, Object obj) {
        final byte[] keyBytes = key.getBytes(charset);
        String json = JSONObject.toJSONString(obj);
        final byte[] valueBytes = json.getBytes(charset);

        boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.setNX(keyBytes, valueBytes));
        return result;
    }

    /***
     *
     * @param key
     * @param obj
     * @param expireTime 单位 秒
     * @param <T>
     * @return
     */
    public <T> boolean putWithExpireTime(String key, T obj, final long expireTime) {
        final byte[] keyBytes = key.getBytes(charset);
        String json = JSONObject.toJSONString(obj);
        final byte[] valueBytes = json.getBytes(charset);

        boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            connection.setEx(keyBytes, expireTime, valueBytes);
            return true;
        });
        return result;
    }

    public <T> boolean putList(String key, List<T> objList) {
        return put(key, objList);
    }

    public <T> boolean putListWithExpireTime(String key, List<T> objList, final long expireTime) {
        return putWithExpireTime(key, objList, expireTime);
    }

    public <T> T get(final String key, Class<T> targetClass) {
        byte[] result = redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key.getBytes(charset)));
        if (result == null) {
            return null;
        }
        return JSONObject.parseObject(new String(result, charset), targetClass);
    }

    public <T> List<T> getList(final String key, Class<T> targetClass) {
        byte[] result = redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key.getBytes(charset)));
        if (result == null) {
            return null;
        }
        return JSONArray.parseArray(new String(result, charset), targetClass);
    }

    /**
     * 精确删除key
     *
     * @param key
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 模糊删除key
     *
     * @param pattern
     */
    public void deleteCacheWithPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 清空所有缓存
     */
    public void clearCache() {
//        deleteCacheWithPattern("*");
    }

    /**
     * 发送订阅消息
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, JSON.toJSONString(message));
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 判断key是否过期
     *
     * @param key
     * @return
     */
    public boolean isExpire(String key) {
        return expire(key) > 0 ? false : true;
    }

    /**
     * 从redis中获取key对应的过期时间;
     * 如果该值有过期时间，就返回相应的过期时间;
     * 如果该值没有设置过期时间，就返回-1;
     * 如果没有该值，就返回-2;
     *
     * @param key
     * @return
     */
    public long expire(String key) {
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }

    public boolean setByLock(String key, String value, long second) {
        Boolean execute = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            Object execute1 = connection.execute("set", key.getBytes(), value.getBytes(), "NX".getBytes(),
                    "EX".getBytes(), String.valueOf(second).getBytes());
            return "OK".equals(execute1);
        });
        return execute;
    }
}
