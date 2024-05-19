package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.tools.PromptTools;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

/**
 * 生成Prompt对象处理器
 * @date 2024/5/17
 */
@Component
public class GenerationPromptProcessor implements MessageProcessor {
    @Override
    public MessageContext process(MessageContext context) {
        Prompt prompt = PromptTools.generatePrompt(context.getContentReq(), context.getDialog(), context.getHistoryMessages());
        context.setPrompt(prompt);
        return context;
    }
}
