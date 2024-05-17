package org.hyizhou.titaniumstation.ai.processor;

import org.springframework.ai.chat.ChatResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天响应处理链
 * @date 2024/5/16
 */
public class ChatRespProcessorChain {
    private final List<ChatRespProcessor> processors = new ArrayList<ChatRespProcessor>();

    public void addProcessor(final ChatRespProcessor processor) {
        processors.add(processor);
    }

    public void removeProcessor(final ChatRespProcessor processor) {
        processors.remove(processor);
    }

    public ChatResponse process(ChatResponse resp) {
        for (final ChatRespProcessor processor : processors) {
            resp = processor.process(resp);
        }
        return resp;
    }

}
