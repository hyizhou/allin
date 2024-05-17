package org.hyizhou.titaniumstation.ai.processor;

import org.springframework.ai.chat.ChatResponse;

/**
 * 聊天响应处理接口
 * @date 2024/5/16
 */
public interface ChatRespProcessor {
    ChatResponse process(ChatResponse chatResponse);
}
