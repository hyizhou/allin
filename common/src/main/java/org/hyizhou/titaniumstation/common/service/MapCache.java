package org.hyizhou.titaniumstation.common.service;

/**
 * @author hyizhou
 * @date 2024/2/8
 */
public interface MapCache<T> {
    /**
     * 保存数据到缓存
     * @param key 缓存的key
     * @param value 缓存的value
     */
    void save(String key, T value);

    /**
     * 获取存入的元素，不存在返回null
     */
    T get(String key);
}
