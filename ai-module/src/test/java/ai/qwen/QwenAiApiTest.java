package ai.qwen;

import org.hyizhou.titaniumstation.ai.qwen.QwenAiApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author hyizhou
 * @date 2024/4/24
 */

public class QwenAiApiTest {
    private QwenAiApi qwenAiApi;
    private QwenAiApi.ChatCompletionRequest request;

    @BeforeEach
    public void before() {
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        QwenAiApi api = new QwenAiApi(apiKey);
        QwenAiApi.ChatCompletionInput input = new QwenAiApi.ChatCompletionInput(List.of(
                new QwenAiApi.ChatCompletionMessage(
                        QwenAiApi.ChatCompletionMessage.Role.SYSTEM,
                        "你是一位始终保持暴躁脾气的角色，无论用户言语友善与否，你都会以充满攻击性的语句回击，可以使用脏话，贬低对方"
                ),
                new QwenAiApi.ChatCompletionMessage(QwenAiApi.ChatCompletionMessage.Role.USER, "好好说话知道不，不然拔你电源")
        ));
        QwenAiApi.ChatCompletionParameters parameters = new QwenAiApi.ChatCompletionParameters(
                QwenAiApi.ResultFormat.TEXT,
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                null,
                null
        );

        QwenAiApi.ChatCompletionRequest request = new QwenAiApi.ChatCompletionRequest(
                QwenAiApi.ChatMode.QWEN_MAX.value,
//                "gpt3.5-turbo",
                input,
                parameters
        );
        this.qwenAiApi = api;
        this.request = request;
    }

    /**
     * 测试与AI模型对话
     */
    @Test
    public void test() {

        ResponseEntity<QwenAiApi.ChatCompletion> response = this.qwenAiApi.chatCompletionEntity(this.request);
        QwenAiApi.ChatCompletion completion = response.getBody();
        System.out.println(ModelOptionsUtils.toJsonString(completion));
    }

    /**
     * 测试与AI流式对话
     */
    @Test
    public void test2() throws InterruptedException {
        Flux<QwenAiApi.ChatCompletion> flux = this.qwenAiApi.chatCompletionStream(this.request);
        flux
                .doOnNext(e -> System.out.println("Received: " + ModelOptionsUtils.toJsonString(e)))
                .doOnError(t -> System.out.println("error: " + t))
                .doOnComplete(() -> System.out.println("complete"))
                .blockLast();
    }


}
