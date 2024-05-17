package org.hyizhou.titaniumstation.ai.qwen.metadata;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.EmptyUsage;
import org.springframework.ai.chat.metadata.Usage;

/**
 * @author hyizhou
 * @date 2024/4/25
 */
public class QwenAiChatResponseMetadata implements ChatResponseMetadata {
    private final String id;
    private final Usage usage;

    public QwenAiChatResponseMetadata(String id, QwenAiUsage usage){
        this.id = id;
        this.usage = usage;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public Usage getUsage() {
        Usage usage = this.usage;
        return usage != null ? usage : new EmptyUsage();
    }
}
