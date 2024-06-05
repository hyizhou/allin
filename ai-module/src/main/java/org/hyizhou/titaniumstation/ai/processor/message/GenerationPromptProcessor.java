package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.config.properties.Constants;
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
                generateOptions(context.getDialog(), context.isAppend())
        );
        context.setPrompt(prompt);
        return context;
    }

    /**
     * 生成ChatOptions对象
     * @param dialog 对话表实体类，包含大语言名称、服务商名称等诸多配置
     * @param stream 是否开启流式输出
     * @return ChatOptions对象，控制大语言模型诸多参数
     */
    private ChatOptions generateOptions(DialogEntity dialog, boolean stream) {
        ChatOptions chatOptions;
        if (dialog.getServiceProvider().equals("qwen")) {

            // withIncrementalOutput 属性表示在流模式下，设置响应方式为增量模式，这是为了以统一不同服务商的响应方式，方便处理
            // 但增量输出模式开启时无法调用函数工具，这是官方 api 所限制
            QwenChatOptions qwenChatOptions = QwenChatOptions.builder().withMode(dialog.getModel()).withIncrementalOutput(stream).build();
            if (Constants.OFFICIAL_QWEN_MODELS.contains(dialog.getModel())){
                // 官方千问模型提供内置搜索功能，但开源模型不支持
                qwenChatOptions.setEnableSearch(true);
            }
            if (dialog.getFunctions() != null) {
                qwenChatOptions.setFunctions(new HashSet<>(dialog.getFunctions()));
            }
            chatOptions = qwenChatOptions;
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
        List<Message> messages = historyMsg.stream().map(entity -> PromptTools.generateMessage(entity.getContent(), entity.getRole())).collect(Collectors.toList());  // 此处不使用 toList 是因为后续需要再进行修改
        messages.add(PromptTools.generateMessage(req.getContent(), req.getRole()));
        return new Prompt(messages, options);
    }
}
