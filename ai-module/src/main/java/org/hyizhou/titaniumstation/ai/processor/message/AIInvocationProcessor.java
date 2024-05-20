package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatClient;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 大语言模型的选择与调用处理器
 * @date 2024/5/17
 */
@Component
public class AIInvocationProcessor implements MessageProcessor {
    private final ApplicationContext applicationContext;

    public AIInvocationProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public MessageContext process(MessageContext context) {
        // 获取服务商名称
        String serviceProvider = context.getDialog().getServiceProvider();
        ChatClient chatClient = chooseChatClient(serviceProvider);
        ChatResponse chatResponse = chatClient.call(context.getPrompt());
        context.setChatResponse(chatResponse);
        context.setRespTime(LocalDateTime.now());
        return context;
    }

    private ChatClient chooseChatClient(String serviceProvider) {
        if ("qwen".equals(serviceProvider)) {
            return applicationContext.getBean(QwenChatClient.class);
        }
        return applicationContext.getBean(OpenAiChatClient.class);
    }
}
