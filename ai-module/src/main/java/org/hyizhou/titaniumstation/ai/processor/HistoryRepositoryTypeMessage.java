package org.hyizhou.titaniumstation.ai.processor;

import org.springframework.ai.chat.messages.Message;

/**
 * 存储类型为 Message 的历史记录
 * @date 2024/5/16
 */
public interface HistoryRepositoryTypeMessage extends HistoryRepository<Message> {
}
