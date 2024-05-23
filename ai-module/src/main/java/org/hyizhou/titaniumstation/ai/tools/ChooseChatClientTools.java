package org.hyizhou.titaniumstation.ai.tools;

import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatClient;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 选择聊天客户端类型
 * @date 2024/5/22
 */
@Component
public class ChooseChatClientTools {
    private final ApplicationContext applicationContext;

    public ChooseChatClientTools(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    public ChatClient choose(String serviceProvider) {
        if ("qwen".equals(serviceProvider)) {
            return applicationContext.getBean(QwenChatClient.class);
        }
        return applicationContext.getBean(OpenAiChatClient.class);
    }

    public StreamingChatClient steamingChoose(String serviceProvider) {
        return (StreamingChatClient) choose(serviceProvider);
    }
}
