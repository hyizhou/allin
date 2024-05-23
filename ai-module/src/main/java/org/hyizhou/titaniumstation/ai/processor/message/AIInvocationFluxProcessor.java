package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.tools.ChooseChatClientTools;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * 大语言模型的选择与调用处理器，不过此处进行的是流式调用
 * @date 2024/5/22
 */
@Component
public class AIInvocationFluxProcessor implements MessageProcessor{
    private final ChooseChatClientTools chooseChatClientTools;

    public AIInvocationFluxProcessor(ChooseChatClientTools chooseChatClientTools) {
        this.chooseChatClientTools = chooseChatClientTools;
    }
    @Override
    public MessageContext process(MessageContext context) {
        String serviceProvider = context.getDialog().getServiceProvider();
        StreamingChatClient chatClient = chooseChatClientTools.steamingChoose(serviceProvider);
        Flux<ChatResponse> flux = chatClient.stream(context.getPrompt());
        context.setFluxChatResponse(flux);
        context.setRespTime(LocalDateTime.now());
        return context;
    }
}
