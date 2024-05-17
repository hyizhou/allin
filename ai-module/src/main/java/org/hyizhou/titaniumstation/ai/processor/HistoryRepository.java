package org.hyizhou.titaniumstation.ai.processor;

import java.util.List;
import java.util.Optional;

/**
 * 历史消息处理器
 * @date 2024/5/16
 */
public interface HistoryRepository<T> {
    /**
     * 保存一条聊天记录
     * @param message 聊天消息对象
     */
    void save(T message);

    /**
     * 追加一条聊天记录
     * @param message 聊天消息对象
     * @return 追加后的聊天历史
     */
    List<T> append(T message);

    /**
     * 添加一批聊天记录
     * @param messages 聊天消息对象列表
     * @return 追加后的聊天历史
     */
    List<T> appendAll(List<T> messages);

    /**
     * 弹出最后的消息并删除
     * @return 聊天消息对象
     */
    Optional<T> pop();

    /**
     * 查找指定消息
     * @return 聊天消息对象
     */
    Optional<T> findMessage(String id);


}
