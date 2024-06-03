package org.hyizhou.titaniumstation.ai.tools;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.exception.NotFoundRoleException;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatOptions;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Prompt 工厂
 * @date 2024/5/17
 */
public class PromptTools {

    public static Prompt generatePrompt(ContentReq req, DialogEntity dialog, List<MessageEntity> messages){
        List<Message> updateMessage = messages.stream().map(entity -> {
            return PromptTools.generateMessage(entity.getContent(), entity.getRole());
        }).collect(Collectors.toList());
        updateMessage.add(generateMessage(req.getContent(), req.getRole()));
        ChatOptions options = generateOptions(dialog.getModel(), dialog.getServiceProvider());
        return new Prompt(updateMessage, options);
    }

    public static Prompt generatePrompt(ContentReq req, DialogEntity dialog){
        ChatOptions options = generateOptions(dialog.getModel(), dialog.getServiceProvider());
        return generatePrompt(req.getContent(), req.getRole(), options);
    }

    public static Prompt generatePrompt(String message, String role, ChatOptions options) {
        Prompt prompt = generatePrompt(message, role);
        return new Prompt(prompt.getInstructions(), options);
    }

    public static Prompt generatePrompt(String message, String role){
        Message msgObj = generateMessage(message, role);
        return new Prompt(msgObj);
    }

    public static Message generateMessage(String message, String role){
        return switch (role){
            case "user" -> new UserMessage(message);
            case "system" -> new SystemMessage(message);
            case "assistant" -> new AssistantMessage(message);
            default ->
                    throw new NotFoundRoleException(role);
        };
    }

    public static ChatOptions generateOptions(String model, String client){
        ChatOptions chatOptions;
        if ("qwen".equals(client)) {
            chatOptions = QwenChatOptions.builder().withMode(model).build();
        }else {
            chatOptions = OpenAiChatOptions.builder().withModel(model).build();
        }
        return chatOptions;
    }

    public static ChatOptions generateOptionsForStream(String model, String client){
        ChatOptions chatOptions;
        if ("qwen".equals(client)) {
            chatOptions = QwenChatOptions.builder().withMode(model)
                    // 本系统统一不使用增量输出，由于qwen默认增量模式，此处需要显式设置，非流式调用添加此参数会报错
                    .withIncrementalOutput(true)
                    .build();
        }else {
            chatOptions = OpenAiChatOptions.builder().withModel(model).build();
        }
        return chatOptions;
    }
}
