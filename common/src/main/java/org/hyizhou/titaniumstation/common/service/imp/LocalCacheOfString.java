package org.hyizhou.titaniumstation.common.service.imp;

import org.hyizhou.titaniumstation.common.service.MapCache;
import org.hyizhou.titaniumstation.common.service.QueueCache;
import org.hyizhou.titaniumstation.common.type.CircularQueue;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 在本地的缓存实现，存储字符数据
 * @author hyizhou
 * @date 2024/2/8
 */
@Component
@Lazy
public class LocalCacheOfString implements QueueCache<String>, MapCache<String> {
    private final Map<String, String> mapCache;
    private final Map<String, Queue<String>> queueCache;

    public LocalCacheOfString() {
        this.mapCache = new HashMap<>();
        this.queueCache = new HashMap<>();
    }

    @Override
    public void save(String key, String value) {
        mapCache.put(key, value);
    }

    @Override
    public String get(String key) {
        return mapCache.get(key);
    }

    @Override
    public void push(String key, String value, int maxSize) {
        if (!queueCache.containsKey(key)) {
            queueCache.put(key, new CircularQueue<>(maxSize));
        }
        Queue<String> queue = queueCache.get(key);
        queue.add(value);
    }

    @Override
    public List<String> toListInRange(String key, int start, int end) {
        List<String> fullList = toList(key);
        // 确保 start 不小于 0，并且不大于列表的大小
        start = Math.max(start, 0);
        // 确保 end 不大于列表的大小
        end = Math.min(end, fullList.size());
        // 确保 safeStart 不大于 safeEnd
        start = Math.min(start, end);
        return fullList.subList(start, end);
    }

    @Override
    public List<String> toList(String key) {
        return new ArrayList<>(queueCache.get(key));
    }
}
