package ai.qwen;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.hyizhou.titaniumstation.ai.config.QwenConfiguration;
import org.hyizhou.titaniumstation.ai.llmTools.Weather;
import org.hyizhou.titaniumstation.ai.qwen.QwenAiApi;
import org.hyizhou.titaniumstation.ai.qwen.QwenChatClient;
import org.hyizhou.titaniumstation.ai.qwen.QwenChatOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.ai.autoconfigure.retry.SpringAiRetryAutoConfiguration;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

/**
 * @author hyizhou
 * @date 2024/4/25
 */
public class QwenChatClientTest {

    private static QwenAiApi qwenAiApi;

    private static final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    RestClientAutoConfiguration.class,
                    QwenConfiguration.class,
                    SpringAiRetryAutoConfiguration.class))
            .withUserConfiguration(config.class);

    @Configuration
    static class config {
        @Bean
        public FunctionCallback weatherFunction(){
            return FunctionCallbackWrapper.builder(new Weather())
                    .withDescription("获取城市天气")
                    .withName("getWeather")
                    .build();
        }
    }

    @BeforeAll
    public static void init() {
        // 获取LoggerContext
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 获取根Logger
        Logger rootLogger = context.getLogger("org.hyizhou");

        // 设置全局日志级别为DEBUG
        rootLogger.setLevel(Level.DEBUG);
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        qwenAiApi = new QwenAiApi(apiKey);
    }

    /**
     * call 方法测试
     */
    @Test
    public void test() {
        QwenChatOptions options = QwenChatOptions.builder().withMode(QwenAiApi.DEFAULT_CHAT_MODEL).withIncrementalOutput(false).build();
        QwenChatClient qwenChatClient = new QwenChatClient(qwenAiApi);
        Prompt prompt = new Prompt("你好", options);
        ChatResponse chatResponse = qwenChatClient.call(prompt);
        System.out.println(chatResponse);
    }

    /**
     * call 方法另一种情况的测试
     */
    @Test
    public void test2(){
        QwenChatOptions options = QwenChatOptions.builder()
                .withMode(QwenAiApi.ChatMode.QWEN_TURBO.value)
                .withIncrementalOutput(false)
                .withResultFormat(QwenAiApi.ResultFormat.MESSAGE)  // 指定消息响应格式
                .withEnableSearch(true)
                .build();
        QwenChatClient qwenChatClient = new QwenChatClient(qwenAiApi);
        Prompt prompt = new Prompt("你好？", options);
        ChatResponse chatResponse = qwenChatClient.call(prompt);
        System.out.println(chatResponse);
    }

    /**
     * 函数调用测试
     */
    @Test
    public void test3(){
        contextRunner.run(context -> {
            var qwenChatClient = context.getBean(QwenChatClient.class);
            QwenChatOptions options = QwenChatOptions.builder()
                    .withMode(QwenAiApi.ChatMode.QWEN_TURBO.value)
                    .build();
            Prompt prompt = new Prompt("明天广州什么天气？", options);
            ChatResponse response = qwenChatClient.call(prompt);
            System.out.println(response.getResult());
        });
    }

    /**
     * 函数调用流式返回
     */
    @Test
    public void test4(){
        contextRunner.run(context -> {
            var qwenChatClient = context.getBean(QwenChatClient.class);
            QwenChatOptions options = QwenChatOptions.builder()
                    .withMode(QwenAiApi.ChatMode.QWEN_TURBO.value)
                    .build();
            Prompt prompt = new Prompt("你好，查询以下广州天气", options);
            Flux<ChatResponse> chatResponseFlux = qwenChatClient.stream(prompt);
            chatResponseFlux.doOnNext(chatResponse -> System.out.println("测试方法输出："+chatResponse.getResult()))
                    .blockLast();
        });
    }

    /**
     * 流式聊天测试
     */
    @Test
    public void test5(){
        contextRunner.run(context -> {
            var qwenChatClient = context.getBean(QwenChatClient.class);
            QwenChatOptions options = QwenChatOptions.builder()
                    .withMode(QwenAiApi.ChatMode.QWEN_TURBO.value)
                    .build();
            Prompt prompt = new Prompt("你好，你哪位？", options);
            Flux<ChatResponse> chatResponseFlux = qwenChatClient.stream(prompt);
            chatResponseFlux.doOnNext(chatResponse -> System.out.println("测试方法输出："+chatResponse.getResult()))
                    .blockLast();
        });
    }
}
