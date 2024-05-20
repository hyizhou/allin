package org.hyizhou.titaniumstation.ai.api;

import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenAiApi;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatClient;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatOptions;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 测试类
 * @author hyizhou
 * @date 2024/5/15
 */
@RestController
@RequestMapping("/api/ai")
public class Test {
    @Autowired
    private QwenChatClient qwenChatClient;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping(path = "/test2",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> getStream(){
        QwenChatOptions options = QwenChatOptions.builder()
                .withMode(QwenAiApi.ChatMode.QWEN_TURBO.value)
                .build();
        Prompt prompt = new Prompt("你好，讲一个故事", options);
        return qwenChatClient.stream(prompt);
    }
}
