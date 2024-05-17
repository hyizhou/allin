package org.hyizhou.titaniumstation.ai.tools;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.exception.NotFoundRoleException;
import org.hyizhou.titaniumstation.ai.qwen.QwenChatOptions;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;

/**
 * Prompt 工厂
 * @date 2024/5/17
 */
public class PromptTools {

    public static Prompt generatePrompt(ContentReq req, DialogEntity dialog){
        ChatOptions options = generateOptions(dialog.getModel(), dialog.getServiceProvider());
        return generatePrompt(req.content(), req.role(), options);
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
}