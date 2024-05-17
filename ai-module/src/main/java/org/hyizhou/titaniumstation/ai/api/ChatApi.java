package org.hyizhou.titaniumstation.ai.api;

import org.hyizhou.titaniumstation.ai.service.AiMainServer;
import org.hyizhou.titaniumstation.common.ai.model.MessageRequest;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author hyizhou
 * @date 2024/5/15
 */
@RestController
@RequestMapping("/api/ai/chat")
public class ChatApi {

    private final AiMainServer aiMainServer;

    public ChatApi(AiMainServer aiMainServer) {
        this.aiMainServer = aiMainServer;
    }

    @PostMapping("/steam")
    public Flux<ChatResponse> doSteamChat(@RequestBody MessageRequest request){
        return aiMainServer.steam(request);
    }

    @PostMapping("/chat")
    public ChatResponse doChat(@RequestBody MessageRequest request){
        return aiMainServer.chat(request);
    }
}
