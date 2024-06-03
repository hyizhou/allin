package org.hyizhou.titaniumstation.ai.llmClient.qwen;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * qwen模型接口
 * 只包含了聊天模型，未包含VL视觉模型、Audio模型等
 * 注意的是，开源模型和官方模型某些参数存在区别，比如官方模型能开启搜索功能，而开源模型并未有此参数
 * @author hyizhou
 * @date 2024/4/23
 */
public class QwenAiApi {
    private final Logger logger = LoggerFactory.getLogger(QwenAiApi.class);
    private final RestClient restClient;
    private final WebClient webClient;
    private static final String DEFAULT_BASE_URL = "https://dashscope.aliyuncs.com";
    public static final String DEFAULT_CHAT_MODEL = ChatMode.QWEN1_5_32B_CHAT.value;

    public QwenAiApi(String apiKey){
        this(DEFAULT_BASE_URL, apiKey);
    }

    public QwenAiApi(String baseUrl, String apiKey){
        this(baseUrl, apiKey,  RestClient.builder());
    }

    public QwenAiApi(String baseUrl, String apiKey, RestClient.Builder restClientBuilder){
        this(baseUrl, apiKey, restClientBuilder, RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
    }

    public QwenAiApi(String baseUrl, String apiKey, RestClient.Builder resultClientBuilder, ResponseErrorHandler responseErrorHandler){
        this.restClient = resultClientBuilder
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> {
                    headers.setBearerAuth(apiKey);
                    headers.setContentType(MediaType.APPLICATION_JSON);
//                    if (isSteam) {
//                        headers.setAccept(List.of(MediaType.TEXT_EVENT_STREAM));
//                    }
                })
                .defaultStatusHandler(responseErrorHandler)
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> {
                    headers.setBearerAuth(apiKey);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }

    public ResponseEntity<ChatCompletion> chatCompletionEntity(ChatCompletionRequest request){
        return chatCompletionEntity(request, null);
    }

    /**
     * 为所给聊天对话构建模型响应
     * @param request 聊天请求
     * @param plugins 插件列表，默认为空，json格式，如 `{"name": {}, "name2":{"param": "value"}}`，注意和函数调用功能区分
     * @return 包含HTTP状态码和响应的实体
     */
    public ResponseEntity<ChatCompletion> chatCompletionEntity(ChatCompletionRequest request, String plugins){
        if (logger.isDebugEnabled()){
            logger.debug("聊天请求体：{}\n", ModelOptionsUtils.toJsonString(request));
        }

        // 流式响应才有此配置
        if (request.parameters() != null && Boolean.TRUE.equals(request.parameters.incrementalOutput())){
            throw new IllegalArgumentException("非流式对话parameters.incrementalOutput不能为true");
        }
        RestClient.RequestBodyUriSpec post = this.restClient.post();
        if (plugins != null){
            post.header("X-DashScope-Plugin", plugins);
        }
        ResponseEntity<ChatCompletion> response = post.uri("/api/v1/services/aigc/text-generation/generation")
                .body(request)
                .retrieve()
                .toEntity(ChatCompletion.class);

        if (logger.isDebugEnabled()){
            logger.debug("聊天响应：{}\n", response);
        }
        return response;
    }

    public Flux<ChatCompletion> chatCompletionStream(ChatCompletionRequest request){
        return chatCompletionStream(request, null);
    }

    /**
     * 为所给聊天对话船舰流式聊天响应
     * @param request 聊天请求
     * @return Flux类型
     */

    public Flux<ChatCompletion> chatCompletionStream(ChatCompletionRequest request, String plugins){
        if (logger.isDebugEnabled()){
            logger.debug("流-聊天请求体：{}\n", ModelOptionsUtils.toJsonString(request));
        }
        WebClient.RequestBodyUriSpec post = this.webClient.post();
        if (StringUtils.hasLength(plugins)){
            post.header("X-DashScope-Plugin", plugins);
        }
        return post
                .uri("/api/v1/services/aigc/text-generation/generation")
                // 流式响应的关键
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM.toString())
                .body(Mono.just(request), ChatCompletionInput.class)
                // 发送响应并取回
                .retrieve()
                .bodyToFlux(String.class)
                .map(x -> ModelOptionsUtils.jsonToObject(x, ChatCompletion.class))
                // output.finish_reason或output.choices[0].finish_reason 为 stop 时断开
                .takeUntil(x -> {
                    if (x.output != null) {
                        return ChatCompletionOutput.FinishReason.STOP == x.output.finishReason
                                || (x.output.choices != null && ChatCompletionOutput.FinishReason.STOP == x.output.choices.get(0).finishReason);
                    }
                    return true;
                });
    }

    public enum ChatMode {
        QWEN1_5_72B_CHAT("qwen1.5-72b-chat"),
        QWEN1_5_32B_CHAT("qwen1.5-32b-chat"),
        QWEN1_5_14B_CHAT("qwen1.5-14b-chat"),
        QWEN1_5_7B_CHAT("qwen1.5-7b-chat"),
        QWEN1_5_1_8B_CHAT("qwen1.5-1.8b-chat"),
        QWEN1_5_0_5B_CHAT("qwen1.5-0.5b-chat"),
        CODEQWEN1_5_7B_CHAT("codeqwen1.5-7b-chat"),
        QWEN_72B_CHAT("qwen-72b-chat"),
        QWEN_14B_CHAT("qwen-14b-chat"),
        QWEN_7B_CHAT("qwen-7b-chat"),
        QWEN_1_8B_LONGCONTEXT_CHAT("qwen-1.8b-longcontext-chat"),
        QWEN_1_8B_CHAT("qwen-1.8b-chat"),
        QWEN_TURBO("qwen-turbo"),
        QWEN_PLUS("qwen-plus"),
        QWEN_MAX("qwen-max"),
        QWEN_MAX_LONG("qwen-max-longcontext");


        public final String value;

        ChatMode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 聊天模型输入的参数实体类
     * @param mode
     * @param input
     * @param parameters
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletionRequest(
            @JsonProperty("model") String mode,
            @JsonProperty("input") ChatCompletionInput input,
            @JsonProperty("parameters") ChatCompletionParameters parameters
    ){
        public ChatCompletionRequest(List<ChatCompletionMessage> messages){
            this(null, new ChatCompletionInput(messages), null);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletionInput(
            // 官方文档中还存在prompt和history字段，但可能会废弃，所以此处干脆不添加了
            @JsonProperty("messages") List<ChatCompletionMessage> messages,
            @JsonProperty("prompt") String prompt
    ){
        public ChatCompletionInput(List<ChatCompletionMessage> messages){
            this(messages, null);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletionMessage(
            @JsonProperty("role") Role role,
            @JsonProperty("content") String content,
            /*
            似乎没有此参数
             */
            @JsonProperty("name") String name,

            /*
            目前开源模型文档中未写支持函数调用，只有官方模型支持
             */
            @JsonProperty("tool_calls") List<ToolCall> toolCalls
    ){

        public ChatCompletionMessage(Role role, String content){
            this(role, content, null, null);
        }
        public enum Role {
            @JsonProperty("user") USER,
            @JsonProperty("assistant") ASSISTANT,
            @JsonProperty("system") SYSTEM,
            @JsonProperty("tool") TOOL;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ToolCall(
            /*
            当前只能是 function
             */
            @JsonProperty("type") String type,
            @JsonProperty("function") ChatCompletionFunction function
    ){}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletionFunction(
            @JsonProperty("name") String name,
            @JsonProperty("arguments") String arguments
    ){}

    public record ChatCompletionParameters(
            // "text"表示旧版本的text，"message"表示兼容openai的message
            @JsonProperty("result_format") ResultFormat resultFormat,
            // 随机种子
            @JsonProperty("seed") Long seed,
            // 限制生成上限
            @JsonProperty("max_tokens") Long maxTokens,
            // 核采样方法的概率阈值，取值范围(0,1.0)
            @JsonProperty("top_p") Float topP,
            // 采样候选集的大小
            @JsonProperty("top_k") Integer topK,
            // 采样温度，取值范围： [0, 2)，值越低越确定
            @JsonProperty("temperature") Float temperature,
            // 模型生成时的重复度，1.0表示不做惩罚。默认为1.1
            @JsonProperty("repetition_penalty") Float repetitionPenalty,
            // 控制流输出模式，默认为False，后续会包含前面已输出内容
            @JsonProperty("incremental_output") Boolean incrementalOutput,
            // 模型内置互联网搜索服务是否开启，目前不支持开源模型系列
            @JsonProperty("enable_search") Boolean enableSearch,
            // 函数调用工具列表，目前不支持开源模型系列
            @JsonProperty("tools") List<FunctionTool> tools
            // 还有stop参数，暂不添加

    ){
        public ChatCompletionParameters(ResultFormat resultFormat){
            this(resultFormat, null, null, null, null, null, null, null, null,null);
        }

    }

    public record FunctionTool(
            @JsonProperty("type") Type type,
            @JsonProperty("function") Function function
    ){
        public FunctionTool(Function function){
            this(Type.FUNCTION, function);
        }
        public enum Type {
            @JsonProperty("function") FUNCTION
        }

        public record Function(
                @JsonProperty("description") String description,
                @JsonProperty("name") String name,
                @JsonProperty("parameters") Map<String, Object> parameters) {

            /**
             * 为了和 FunctionCallback 接口兼容，减少转换成本，所以第三个参数改成字符型
             *
             * @param description tool function description.
             * @param name tool function name.
             * @param jsonSchema tool function schema as json.
             */
            @ConstructorBinding
            public Function(String description, String name, String jsonSchema) {
                this(description, name, ModelOptionsUtils.jsonToMap(jsonSchema));
            }
        }
    }

    public enum ResultFormat {
        @JsonProperty("text") TEXT,
        @JsonProperty("message") MESSAGE;
    }

    /**
     * 大语言模型根据输入参数返回的聊天完成响应
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletion(
            /*
            错误代码，只在出现错误时才出现
             */
            @JsonProperty("code") String code,
            /*
            错误信息，只在出现错误时才出现
             */
            @JsonProperty("message") String message,
            @JsonProperty("request_id") String requestId,
            @JsonProperty("output") ChatCompletionOutput output,
            @JsonProperty("usage") Usage usage
    ){}
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Usage(
            @JsonProperty("output_tokens") Integer outputTokens,
            @JsonProperty("input_tokens") Integer inputTokens,
            @JsonProperty("total_tokens") Integer totalTokens
    ){}
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletionOutput(
            @JsonProperty("text") String text,
            @JsonProperty("finish_reason") FinishReason finishReason,
            @JsonProperty("choices") List<Choice> choices

    ){
        public enum FinishReason {
            // 生成结束并停止
            @JsonProperty("stop") STOP,
            // 生成过长
            @JsonProperty("length") LENGTH,
            // 正在生成
            @JsonProperty("null") NULL,
            // 函数调用
            @JsonProperty("tool_calls") ToolCalls;
        }

        /**
         * 此字段在请求时将 parameters.result_format 设置为 message 才出现
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record Choice(
                @JsonProperty("finish_reason") FinishReason finishReason,
                @JsonProperty("message") ChatCompletionMessage message

        ){}
    }
}
