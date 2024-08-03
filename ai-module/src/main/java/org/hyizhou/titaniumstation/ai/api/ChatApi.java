package org.hyizhou.titaniumstation.ai.api;

import org.hyizhou.titaniumstation.ai.service.ChatService;
import org.hyizhou.titaniumstation.ai.service.SimpleChatService;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.request.MessageRequest;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author hyizhou
 * @date 2024/5/15
 */
@RestController
@RequestMapping("/api/ai")
public class ChatApi {

    private final SimpleChatService simpleChatService;
    private final ChatService chatService;

    public ChatApi(SimpleChatService simpleChatService, ChatService chatService) {
        this.simpleChatService = simpleChatService;
        this.chatService = chatService;
    }

    @PostMapping("/dialogs/{dialogs_id}/steam")
    public Flux<ContentResp> doSteamChat(@PathVariable("dialogs_id")String dialogId, @RequestBody ContentReq req){
        req.setDialogId(dialogId);
        return chatService.streamChat(req);
    }

    @PostMapping("/dialogs/{dialogs_id}/chat")
    public ContentResp doChat(@PathVariable("dialogs_id")String dialogId, @RequestBody ContentReq req){
        req.setDialogId(dialogId);
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

    @PostMapping(value = "/simpleStreamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> doSimpleStreamChat(@RequestBody MessageRequest request){
        return simpleChatService.steam(request);
    }


}
