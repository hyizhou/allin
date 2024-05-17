package org.hyizhou.titaniumstation.common.service;

import java.util.List;

/**
 * 提供接口用于缓存获取到的系统信息
 * @author hyizhou
 * @date 2024/2/4
 */
public interface QueueCache<T> {
    /**
     * 缓存元素到队列，可以设置长度、滚动更新
     * @param key 缓存的key
     * @param value 缓存的value
     * @param maxSize 最大长度
     */
    void push(String key, T value, int maxSize);

    /**
     * 获取存入元素的队列
      */
    List<T> toListInRange(String key, int start, int end);

    /**
     * 取出缓存队列中所有元素
     */
    List<T> toList(String key);
}
