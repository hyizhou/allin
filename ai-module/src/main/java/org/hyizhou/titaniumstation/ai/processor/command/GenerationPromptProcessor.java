package org.hyizhou.titaniumstation.ai.processor.command;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.processor.MessageProcessor;
import org.hyizhou.titaniumstation.ai.tools.PromptTools;
import org.springframework.ai.chat.prompt.Prompt;

/**
 * 生成Prompt对象处理器
 * @date 2024/5/17
 */
public class GenerationPromptProcessor implements MessageProcessor {
    @Override
    public MessageContext process(MessageContext context) {
        Prompt prompt = PromptTools.generatePrompt(context.getContentReq(), context.getDialog(), context.getMessages());
        context.setPrompt(prompt);
        return context;
    }
}
