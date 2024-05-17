package org.hyizhou.titaniumstation.ai.processor.imp;

import org.hyizhou.titaniumstation.ai.processor.HistoryRepositoryTypeMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 对存储在List中的历史进行操作，主要用于测试
 * @date 2024/5/16
 */
@Component
public class ListHistoryRepository implements HistoryRepositoryTypeMessage {
    private final List<Message> history = new LinkedList<>();
    @Override
    public void save(Message message) {
        history.add(message);
    }

    @Override
    public List<Message> append(Message message) {
        this.save(message);
        return history;
    }

    @Override
    public List<Message> appendAll(List<Message> messages) {
        this.history.addAll(messages);
        return history;
    }

    @Override
    public Optional<Message> pop() {
        if (history.isEmpty()) {
            return Optional.empty();
        }
        Message message = history.remove(history.size() - 1);
        return Optional.ofNullable(message);
    }

    @Override
    public Optional<Message> findMessage(String id) {
        if (history.isEmpty()){
            return Optional.empty();
        }
        Message message = history.get(Integer.parseInt(id));
        return Optional.ofNullable(message);
    }
}
