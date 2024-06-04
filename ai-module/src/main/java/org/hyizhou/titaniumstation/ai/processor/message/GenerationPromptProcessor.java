package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatOptions;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.tools.PromptTools;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生成Prompt对象处理器
 * @date 2024/5/17
 */
@Component
public class GenerationPromptProcessor implements MessageProcessor {
    @Override
    public MessageContext process(MessageContext context) {
//        Prompt prompt = PromptTools.generatePrompt(context.getContentReq(), context.getDialog(), context.getHistoryMessages());
        Prompt prompt = generatePrompt(
                context.getContentReq(),
                context.getHistoryMessages(),
                generateOptions(context.getDialog())
        );
        context.setPrompt(prompt);
        return context;
    }

    /**
     * 生成ChatOptions对象
     * @param dialog 对话表实体类，包含大语言名称、服务商名称等诸多配置
     * @return ChatOptions对象，控制大语言模型诸多参数
     */
    private ChatOptions generateOptions(DialogEntity dialog) {
        ChatOptions chatOptions;
        if (dialog.getServiceProvider().equals("qwen")) {
            chatOptions = QwenChatOptions.builder().withMode(dialog.getModel()).build();
            if (dialog.getFunctions() != null) {
                ((QwenChatOptions) chatOptions).setFunctions(new HashSet<>(dialog.getFunctions()));
            }
        } else {
            chatOptions = OpenAiChatOptions.builder().withModel(dialog.getModel()).build();
            if (dialog.getFunctions() != null) {
                ((OpenAiChatOptions) chatOptions).setFunctions(new HashSet<>(dialog.getFunctions()));
            }
        }
        return chatOptions;
    }

    /**
     * 生成Prompt对象
     * @param req 用户当前请求的请求体，将从其中取出消息内容
     * @param historyMsg 用户聊天历史消息
     * @param options 大语言模型控制参数
     * @return Prompt对象
     */
    private Prompt generatePrompt(ContentReq req, List<MessageEntity> historyMsg, ChatOptions options) {
        List<Message> messages = historyMsg.stream().map(entity -> {
            return PromptTools.generateMessage(entity.getContent(), entity.getRole());
        }).collect(Collectors.toList());  // 此处不使用 toList 是因为后续需要再进行修改
        messages.add(PromptTools.generateMessage(req.getContent(), req.getRole()));
        return new Prompt(messages, options);
    }
}
