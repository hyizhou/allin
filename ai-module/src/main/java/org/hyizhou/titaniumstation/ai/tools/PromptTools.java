package org.hyizhou.titaniumstation.ai.tools;

import org.hyizhou.titaniumstation.ai.config.properties.Constants;
import org.hyizhou.titaniumstation.ai.exception.NotFoundRoleException;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatOptions;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;

import java.util.HashSet;
import java.util.List;

/**
 * Prompt 工厂
 * @date 2024/5/17
 */
public class PromptTools {

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

    public static ChatOptions generateOptions(String model, String client, List<String> functions, boolean stream){
        functions = functions == null ? List.of() : functions;
        ChatOptions chatOptions;
        if ("qwen".equals(client)) {
            // qwen 开启 incremental_output 时与 tools 无法同时使用，也就是开启增量输出时无法进行函数工具调用
            // 但官方给大模型内置了搜索服务可供使用
            QwenChatOptions qwenChatOptions = QwenChatOptions.builder().withMode(model).withIncrementalOutput(stream).build();
            if (Constants.OFFICIAL_QWEN_MODELS.contains(model)){
                qwenChatOptions.setEnableSearch(true);
            }
            qwenChatOptions.setFunctions(new HashSet<>(functions));
            chatOptions = qwenChatOptions;
        }else {
            chatOptions = OpenAiChatOptions.builder().withModel(model).build();
            ((OpenAiChatOptions) chatOptions).setFunctions(new HashSet<>(functions));
        }
        return chatOptions;
    }
}
