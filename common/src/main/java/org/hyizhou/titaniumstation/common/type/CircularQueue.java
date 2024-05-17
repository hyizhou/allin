package org.hyizhou.titaniumstation.common.type;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 可设置最大大小，满时，将移除队列头部（最旧）的元素的队列
 *
 * @author hyizhou
 * @date 2024/2/8
 */
public class CircularQueue <E> extends LinkedBlockingQueue<E> {
    public CircularQueue(int maxSize) {
        super(maxSize);
    }

    @Override
    public boolean offer(E e) {
        // 当队列满时，移除队列头部（最旧）的元素
        if (remainingCapacity() == 0) {
            poll();
        }
        return super.offer(e);
    }
}
