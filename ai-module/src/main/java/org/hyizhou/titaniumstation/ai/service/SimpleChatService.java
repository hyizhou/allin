package org.hyizhou.titaniumstation.ai.service;


import org.hyizhou.titaniumstation.ai.qwen.QwenChatClient;
import org.hyizhou.titaniumstation.ai.tools.PromptTools;
import org.hyizhou.titaniumstation.common.ai.request.MessageRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * 简单、临时的聊天服务
 * @author hyizhou
 * @date 2024/5/15
 */
@Service
public class SimpleChatService {
    private final ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(SimpleChatService.class);

    public SimpleChatService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ChatResponse chat(Prompt prompt, String serviceProvider){
        ChatClient chatClient = getChatClientByClient(serviceProvider);
        return chatClient.call(prompt);
    }

    public ChatResponse chat(MessageRequest messageRequest){
        ChatClient chatClient = getChatClientByClient(messageRequest.client());
        ChatOptions options = generateOptions(messageRequest.modelName(), messageRequest.client());
        Prompt prompt = generatePrompt(messageRequest.message(), messageRequest.role(), options);
        return chatClient.call(prompt);
    }

    public Flux<ChatResponse> steam(MessageRequest messageRequest){
        StreamingChatClient chatClient = getStreamingChatClientByClient(messageRequest.client());
        ChatOptions options = generateOptions(messageRequest.modelName(), messageRequest.client());
        Prompt prompt = generatePrompt(messageRequest.message(), messageRequest.role(), options);
        return chatClient.stream(prompt);
    }




    private ChatClient getChatClientByClient(String client) {
        return (ChatClient) getClientByClient(client);

    }


    private StreamingChatClient getStreamingChatClientByClient(String client) {
        return getClientByClient(client);

    }

    /**
     * 通过模型名获取服务商流式客户端
     * @param client 固定格式："xxx/yyy"，比如"qwen/qwen-max"
     * @return 指定服务商聊天客户端
     */
    @NotNull
    private StreamingChatClient getClientByClient(String client) {
        if ("qwen".equals(client)) {
            return applicationContext.getBean(QwenChatClient.class);
        }
        return applicationContext.getBean(OpenAiChatClient.class);
    }



    private Prompt generatePrompt(String message, String role, ChatOptions options) {
        return PromptTools.generatePrompt(message, role, options);
    }


    private ChatOptions generateOptions(String model, String client){
        return PromptTools.generateOptions(model, client);
    }
}
