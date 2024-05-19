package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;

/**
 * 对话聊天处理器
 * @date 2024/5/17
 */
public interface MessageProcessor {
    MessageContext process(MessageContext context);
}
