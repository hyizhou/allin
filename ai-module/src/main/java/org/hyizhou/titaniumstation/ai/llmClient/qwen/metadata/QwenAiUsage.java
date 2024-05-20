package org.hyizhou.titaniumstation.ai.llmClient.qwen.metadata;

import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenAiApi;
import org.springframework.ai.chat.metadata.Usage;

/**
 * qwen 模型用量统计
 * @author hyizhou
 * @date 2024/4/25
 */
public class QwenAiUsage implements Usage {
    private final QwenAiApi.Usage usage;
    public QwenAiUsage(QwenAiApi.Usage usage){
        this.usage = usage;
    }
    @Override
    public Long getPromptTokens() {
        return usage.inputTokens().longValue();
    }

    @Override
    public Long getGenerationTokens() {
        return usage.outputTokens().longValue();
    }
}
