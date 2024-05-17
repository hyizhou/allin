package org.hyizhou.titaniumstation.ai.processor.command;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.processor.MessageProcessor;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.MessageType;

/**
 * 生成响应对象处理器
 * @date 2024/5/17
 */
public class GenerationRespProcessor implements MessageProcessor {
    @Override
    public MessageContext process(MessageContext context) {
        ChatResponse chatResponse = context.getChatResponse();
        ContentResp resp = new ContentResp(
                chatResponse.getResult().getOutput().getContent(),
                "text",
                MessageType.ASSISTANT.getValue(),
                null,  // 对话id先空着，因为现有的代码结构没法将此值取回来
                chatResponse.getResult().getMetadata().getFinishReason(),
                chatResponse.getMetadata().getUsage().getPromptTokens(),
                chatResponse.getMetadata().getUsage().getGenerationTokens()
        );
        context.setContentResp(resp);
        return context;
    }
}
