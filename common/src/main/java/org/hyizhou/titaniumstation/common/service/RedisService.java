package org.hyizhou.titaniumstation.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hyizhou.titaniumstation.common.tools.SimpleJsonTool;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/13
 */
@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate){
        this.redisTemplate = stringRedisTemplate;
    }

    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 列表插入元素，可指定大小，实现滚动更新
     *
     * @param key 键
     * @param value 值
     * @param maxSize 最大长度
     */
    public void leftPush(String key, String value,int maxSize){
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        listOps.leftPush(key, value);
        listOps.trim(key,0,maxSize - 1);
    }

    public void leftPush(String key, Object value, int maxSize) throws JsonProcessingException {
        leftPush(key, SimpleJsonTool.toString(value), maxSize);
    }

    public List<String> listRange(String key, long start, long end){
        ListOperations<String, String> list = redisTemplate.opsForList();
        return list.range(key, start, end);
    }


}
