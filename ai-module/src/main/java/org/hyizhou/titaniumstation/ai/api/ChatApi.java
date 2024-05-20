package org.hyizhou.titaniumstation.ai.api;

import org.hyizhou.titaniumstation.ai.service.SimpleChatService;
import org.hyizhou.titaniumstation.ai.service.ChatService;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.request.MessageRequest;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author hyizhou
 * @date 2024/5/15
 */
@RestController
@RequestMapping("/api/ai/chat")
public class ChatApi {

    private final SimpleChatService simpleChatService;
    private final ChatService chatService;

    public ChatApi(SimpleChatService simpleChatService, ChatService chatService) {
        this.simpleChatService = simpleChatService;
        this.chatService = chatService;
    }

    @PostMapping("/steam")
    public Flux<ChatResponse> doSteamChat(@RequestBody MessageRequest request){
        return simpleChatService.steam(request);
    }

    @PostMapping("/chat")
    public ContentResp doChat(@RequestBody ContentReq req){
        return chatService.chat(req);
    }

    /**
     * 提供临时的、无记忆的、一次性的对话
     * @param request 请求，包含服务商、模型名、内容
     * @return 大语言模型响应
     */
    @PostMapping("/simpleChat")
    public ChatResponse doSimpleChat(@RequestBody MessageRequest request){
        return simpleChatService.chat(request);
    }

    @PostMapping("/simpleStreamChat")
    public Flux<ChatResponse> doSimpleStreamChat(@RequestBody MessageRequest request){
        return simpleChatService.steam(request);
    }

}
