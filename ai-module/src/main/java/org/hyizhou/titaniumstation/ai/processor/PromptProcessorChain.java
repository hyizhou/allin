package org.hyizhou.titaniumstation.ai.processor;

import org.springframework.ai.chat.prompt.Prompt;

import java.util.ArrayList;
import java.util.List;

/**
 * prompt处理链
 * @date 2024/5/16
 */
public class PromptProcessorChain {
    private final List<PromptProcessor> processors = new ArrayList<PromptProcessor>();

    public void addProcessor(final PromptProcessor processor) {
        processors.add(processor);
    }

    public void removeProcessor(final PromptProcessor processor) {
        processors.remove(processor);
    }

    public Prompt process(Prompt prompt) {
        for (final PromptProcessor processor : processors) {
            prompt = processor.process(prompt);
        }
        return prompt;
    }
}
