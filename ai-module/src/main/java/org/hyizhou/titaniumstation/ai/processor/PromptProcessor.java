package org.hyizhou.titaniumstation.ai.processor;

import org.springframework.ai.chat.prompt.Prompt;

/**
 * prompt处理器接口，在传递给大语言模型或接口之前进行处理
 * @date 2024/5/16
 */
public interface PromptProcessor {
    Prompt process(Prompt prompt);
}
