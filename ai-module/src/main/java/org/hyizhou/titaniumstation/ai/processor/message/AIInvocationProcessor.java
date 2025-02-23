package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.tools.ChooseChatClientTools;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 大语言模型的选择与调用处理器
 * @date 2024/5/17
 */
@Component
public class AIInvocationProcessor implements MessageProcessor {
    private final ChooseChatClientTools chooseChatClientTools;

    public AIInvocationProcessor(ChooseChatClientTools chooseChatClientTools) {
        this.chooseChatClientTools = chooseChatClientTools;
    }

    @Override
    public MessageContext process(MessageContext context) {
        // 获取服务商名称
        String serviceProvider = context.getDialog().getServiceProvider();
        ChatClient chatClient = chooseChatClientTools.choose(serviceProvider);
        ChatResponse chatResponse = chatClient.call(context.getPrompt());
        context.setChatResponse(chatResponse);
        context.setRespTime(LocalDateTime.now());
        return context;
    }

}
