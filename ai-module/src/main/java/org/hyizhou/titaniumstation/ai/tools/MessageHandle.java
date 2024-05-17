package org.hyizhou.titaniumstation.ai.tools;

import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * 消息加工
 * @author hyizhou
 * @date 2024/5/15
 */
public interface MessageHandle {
    /**
     * 将新消息添加到消息消息列表，返回最新的消息队列
     * @param message 新消息
     * @return 消息列表
     */
    List<Message> append(Message message);
}
