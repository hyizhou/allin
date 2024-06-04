package org.hyizhou.titaniumstation.ai.llmClient.qwen;

import org.hyizhou.titaniumstation.ai.function.AbstractFunctionCallSupportPro;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.metadata.QwenAiChatResponseMetadata;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.metadata.QwenAiUsage;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通义千问聊天模型
 *
 * @author hyizhou
 * @date 2024/4/22
 */
public class QwenChatClient
        extends AbstractFunctionCallSupportPro<QwenAiApi.ChatCompletionMessage, QwenAiApi.ChatCompletion, QwenAiApi.ChatCompletionRequest, ResponseEntity<QwenAiApi.ChatCompletion>>
        implements ChatClient, StreamingChatClient {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(QwenChatClient.class);
    private final QwenAiApi qwenAiApi;
    /**
     * 用于模型聊天的默认配置
     */
    private final QwenChatOptions defaultOptions;

    /**
     * 对模型聊天的重试模板，策略来自配置
     */
    private final RetryTemplate retryTemplate;

    public QwenChatClient(QwenAiApi qwenAiApi) {
        this(
                qwenAiApi,
                QwenChatOptions.builder().withMode(QwenAiApi.DEFAULT_CHAT_MODEL).build(),
                // Spring AI 框架内提供的默认重试策略
                RetryUtils.DEFAULT_RETRY_TEMPLATE,
                null
        );
    }

    /**
     * 构造函数
     * @param functionCallbackContext 用于从Spring容器中获取到“回调函数”，并解析其注解
     */
    public QwenChatClient(QwenAiApi qwenAiApi, QwenChatOptions defaultOptions, RetryTemplate retryTemplate, FunctionCallbackContext functionCallbackContext) {
        super(functionCallbackContext);
        this.qwenAiApi = qwenAiApi;
        this.defaultOptions = defaultOptions;
        this.retryTemplate = retryTemplate;
    }

    /**
     * 创建AI模型请求参数实体类
     */
    QwenAiApi.ChatCompletionRequest createRequest(Prompt prompt) {
        // 函数工具表
        Set<String> functionsForThisRequest = new HashSet<>();
        QwenAiApi.ChatCompletionParameters parameters = null;
        String model = null;

        List<QwenAiApi.ChatCompletionMessage> chatCompletionMessages = prompt.getInstructions()
                .stream()
                .map(m -> new QwenAiApi.ChatCompletionMessage(
                                QwenAiApi.ChatCompletionMessage.Role.valueOf(m.getMessageType().name()),
                                m.getContent()
                        )
                )
                .toList();



        // 将配置的参数同步到请求实体类中
        if (this.defaultOptions != null) {
            // prompt中的参数覆盖默认参数
            parameters = ModelOptionsUtils.merge(this.defaultOptions, null, QwenAiApi.ChatCompletionParameters.class);
            model = this.defaultOptions.getModel();
            // 获取函数工具名，第二个参数为true（默认）表示将所获取到的回调函数名称全都返回（此处后续代码会根据这些名称将函数传递给模型），若为false，则只返回显式声明(option.functions的值)传递给模型的函数
            Set<String> promptEnableFunctions = this.handleFunctionCallbackConfigurations(this.defaultOptions, false);
            functionsForThisRequest.addAll(promptEnableFunctions);
            logger.debug("defaultOptions中函数名：{}\n", promptEnableFunctions);
        }

        if (prompt.getOptions() != null) {
            if (prompt.getOptions() instanceof ChatOptions runtimeOptions) {
                // QwenAiChatOptions中匹配项都会复制过来
                QwenChatOptions updatedRuntimeOptions = ModelOptionsUtils.copyToTarget(runtimeOptions,
                        ChatOptions.class, QwenChatOptions.class);

                Set<String> promptEnableFunctions = this.handleFunctionCallbackConfigurations(updatedRuntimeOptions, IS_RUNTIME_CALL);
                logger.debug("prompt中函数名：{}\n", promptEnableFunctions);
                functionsForThisRequest.addAll(promptEnableFunctions);

                parameters = ModelOptionsUtils.merge(updatedRuntimeOptions, parameters, QwenAiApi.ChatCompletionParameters.class);
                model = updatedRuntimeOptions.getModel();
            } else {
                throw new IllegalArgumentException("Prompt options are not of type ChatOptions: "
                        + prompt.getOptions().getClass().getSimpleName());
            }
        }

        // 添加函数工具到tools
        if (!CollectionUtils.isEmpty(functionsForThisRequest)){

            List<FunctionCallback> functionCallbacks = this.resolveFunctionCallbacks(functionsForThisRequest);
            logger.debug("functionCallbacks：{}\n", functionCallbacks);
            // 将顶层工具函数格式转换成请求所需格式
            List<QwenAiApi.FunctionTool> tools = functionCallbacks.stream().map(functionCallback -> {
                var function = new QwenAiApi.FunctionTool.Function(
                        functionCallback.getDescription(),
                        functionCallback.getName(),
                        functionCallback.getInputTypeSchema()
                );
                return new QwenAiApi.FunctionTool(function);
            }).toList();
            parameters = ModelOptionsUtils.merge(
                    QwenChatOptions.builder().withTools(tools).build()
                    , parameters, QwenAiApi.ChatCompletionParameters.class);

        }

        return new QwenAiApi.ChatCompletionRequest(model, new QwenAiApi.ChatCompletionInput(chatCompletionMessages), parameters);

    }

    @Override
    public ChatResponse call(Prompt prompt) {
        QwenAiApi.ChatCompletionRequest request = createRequest(prompt);

        String plugins;
        if (this.defaultOptions != null && defaultOptions.getPlugins() != null) {
            plugins = ModelOptionsUtils.toJsonString(defaultOptions.getPlugins());
        } else {
            plugins = null;
        }

        return this.retryTemplate.execute(ctx -> {
            ResponseEntity<QwenAiApi.ChatCompletion> response;
            // 根据官网文档，开源模型不支持函数调用，但支持插件；而非开源版本支持函数调用却未见插件支持，因此如下编写代码
            if (plugins != null){
                response = this.qwenAiApi.chatCompletionEntity(request, plugins);
            }else {
                response = this.callWithFunctionSupport(request);
            }
            QwenAiApi.ChatCompletion body = response.getBody();

            // 响应体为空
            if (body == null) {
                logger.warn("从此聊天提示此无任何返回: {}", prompt);
                return new ChatResponse(List.of());
            }

            String id = body.requestId();
            HashMap<String, Object> properties = new HashMap<>();
            properties.put("id", id);
            properties.put("role", QwenAiApi.ChatCompletionMessage.Role.ASSISTANT.name());


            // parameters 中 result_format 值不同响应中消息位置会不同
            // result_format（返回格式）设置为 Text 时，大模型也能意识到要进行函数调用，但直接text中输出大模型原始文本，不会被解析成函数调用专门格式
            if (request.parameters() == null ||
                    request.parameters().resultFormat() == QwenAiApi.ResultFormat.TEXT ||
                    request.parameters().resultFormat() == null) {
                properties.put("finishReason", body.output().finishReason().name());
                String content = body.output().text();
                Generation generation = new Generation(content, properties)
                        .withGenerationMetadata(ChatGenerationMetadata.from(body.output().finishReason().name(), null));
                return new ChatResponse(
                        List.of(generation),
                        new QwenAiChatResponseMetadata(id, new QwenAiUsage(body.usage()))
                );
            }
            if (request.parameters().resultFormat() == QwenAiApi.ResultFormat.MESSAGE) {
                Generation generation = null;
                List<QwenAiApi.ChatCompletionOutput.Choice> choices = body.output().choices();
                List<Generation> generations = choices.stream().map(
                        choice -> {
                            properties.put("finishReason", choice.finishReason().name());
                            return new Generation(choice.message().content(), properties)
                                    .withGenerationMetadata(ChatGenerationMetadata.from(choice.finishReason().name(), null));
                        }
                ).toList();

                return new ChatResponse(
                        generations,
                        new QwenAiChatResponseMetadata(id, new QwenAiUsage(body.usage()))
                );
            }

            throw new IllegalArgumentException("resultFormat输入了意外的值: " + request.parameters().resultFormat());
        });
    }

    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
        QwenAiApi.ChatCompletionRequest request = createRequest(prompt);

        // 响应的格式，这关系到后面处理方式的不同
        QwenAiApi.ResultFormat resultFormat = Optional.ofNullable(request.parameters())
                .map(QwenAiApi.ChatCompletionParameters::resultFormat)
                .orElse(QwenAiApi.ResultFormat.TEXT);

        String plugins;
        if (this.defaultOptions != null && defaultOptions.getPlugins() != null) {
            plugins = ModelOptionsUtils.toJsonString(defaultOptions.getPlugins());
        } else {
            plugins = null;
        }

        return this.retryTemplate.execute(x -> {
            Flux<QwenAiApi.ChatCompletion> completionFlux;
            if (plugins != null){
                // 使用插件时不进行函数调用
                completionFlux =  this.qwenAiApi.chatCompletionStream(request, plugins);
            }else {
                completionFlux = this.callSteamWithFunctionSupport(request);
            }
            return completionFlux.map( chatCompletion -> {
                return new ChatResponse(
                        generation1(chatCompletion),
                        new QwenAiChatResponseMetadata(chatCompletion.requestId(), new QwenAiUsage(chatCompletion.usage()))
                );
            });
        });
    }

    private List<Generation> generation1(QwenAiApi.ChatCompletion chatCompletion){
        ConcurrentHashMap<String, String> roleMap = new ConcurrentHashMap<>();
        String id = chatCompletion.requestId();
        return chatCompletion.output().choices().stream().map(choice -> {
            if (choice.message().role() != null) {
                roleMap.putIfAbsent(id, choice.message().role().name());  // 搞这么复杂存储role，是担心并非次流元素中都包含了role
            }

            String finish = (choice.finishReason() != null ? choice.finishReason().name() : "");
            var generation = new Generation(choice.message().content(),
                    Map.of("id", id, "role", roleMap.get(id), "finishReason", finish));
            if (choice.finishReason() != null) {
                generation = generation.withGenerationMetadata(
                        ChatGenerationMetadata.from(choice.finishReason().name(), null)
                );
            }
            return generation;
        }).toList();
    }

    /**
     * 对函数进行调用，并将返回结果包装成新地请求实体类
     * @param previousRequest 请求实体类
     * @param responseMessage 响应实体类
     * @param conversationHistory 会话历史记录
     * @return 封装函数调用结果后新地请求实体类
     */
    @Override
    protected QwenAiApi.ChatCompletionRequest doCreateToolResponseRequest(QwenAiApi.ChatCompletionRequest previousRequest, QwenAiApi.ChatCompletionMessage responseMessage, List<QwenAiApi.ChatCompletionMessage> conversationHistory) {
        // 可能会产生多个工具函数调用
        for (QwenAiApi.ToolCall toolCall : responseMessage.toolCalls()) {

            var functionName = toolCall.function().name();
            String functionArguments = toolCall.function().arguments();

            if (!this.functionCallbackRegister.containsKey(functionName)) {
                throw new IllegalStateException("函数名称未找到回调函数: " + functionName);
            }

            String functionResponse = this.functionCallbackRegister.get(functionName).call(functionArguments);

            // 添加函数详细到历史会话
            conversationHistory.add(
                    new QwenAiApi.ChatCompletionMessage(QwenAiApi.ChatCompletionMessage.Role.TOOL,functionResponse, functionName, null)
            );
        }

        // 调用结果和历史消息整合成新地请求，并迭代调用，直到大模型不在需要进行函数调用
        QwenAiApi.ChatCompletionRequest newRequest = new QwenAiApi.ChatCompletionRequest(conversationHistory);
        newRequest = ModelOptionsUtils.merge(newRequest, previousRequest, QwenAiApi.ChatCompletionRequest.class);

        return newRequest;
    }

    /**
     * 从请求实体类中提取出消息实体类列表
     * @param request 请求实体类
     * @return 消息实体类列表
     */
    @Override
    protected List<QwenAiApi.ChatCompletionMessage> doGetUserMessages(QwenAiApi.ChatCompletionRequest request) {
        return request.input().messages();
    }

    /**
     * 从响应中提取出函数调用的信息
     * @param response 响应实体类
     * @return 消息实体类，会包含函数调用的信息
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    protected QwenAiApi.ChatCompletionMessage doGetToolResponseMessage(ResponseEntity<QwenAiApi.ChatCompletion> response) {
        // 只有需要函数调用才会调用本方法，因此无需判断是否未空
        return response.getBody().output().choices().iterator().next().message();
    }

    /**
     * 发出一次聊天
     * @param request 请求实体类
     * @return 响应实体类，包括响应码这些
     */
    @Override
    protected ResponseEntity<QwenAiApi.ChatCompletion> doChatCompletion(QwenAiApi.ChatCompletionRequest request) {
        return this.qwenAiApi.chatCompletionEntity(request);
    }

    // 此方法在 Spring AI 1.0.0 后添加
//    @Override
    protected Flux<ResponseEntity<QwenAiApi.ChatCompletion>> doChatCompletionStream(QwenAiApi.ChatCompletionRequest request) {
        return this.doStreamChatCompletion(request)
                .map(e -> ResponseEntity.of(Optional.of(e)));

    }

    /**
     * 判断是否为函数调用
     * @param response 响应实体类
     * @return 是否为函数调用
     */
    @Override
    protected boolean isToolFunctionCall(ResponseEntity<QwenAiApi.ChatCompletion> response) {
        QwenAiApi.ChatCompletion body = response.getBody();

        return isToolFunction(body)  //只要这里是True那就不可能出现空指针
                && body.output().choices().get(0).finishReason() == QwenAiApi.ChatCompletionOutput.FinishReason.ToolCalls;  // 此句让流式响应未完成时不会进行函数调用
    }


    @Override
    protected Flux<QwenAiApi.ChatCompletion> doStreamChatCompletion(QwenAiApi.ChatCompletionRequest request) {
        Flux<QwenAiApi.ChatCompletion> completionFlux = this.qwenAiApi.chatCompletionStream(request);
        return completionFlux.doOnNext(e -> {
            // 用来 debug
            if (logger.isDebugEnabled()) {
                logger.debug("流-响应：{}\n", ModelOptionsUtils.toJsonString(e));
            }
        });
    }

    @Override
    protected boolean isToolFunction(QwenAiApi.ChatCompletion body) {
        if (body == null) {
            return false;
        }
        QwenAiApi.ChatCompletionOutput output = body.output();

        if (output == null) {
            return false;
        }
        var choices = output.choices();
        if (CollectionUtils.isEmpty(choices)) {
            return false;
        }

        var choice = choices.get(0);
        return !CollectionUtils.isEmpty(choice.message().toolCalls());
    }

    @Override
    protected ResponseEntity<QwenAiApi.ChatCompletion> warpWith(QwenAiApi.ChatCompletion completion){
        return ResponseEntity.of(Optional.of(completion));
    }
}
