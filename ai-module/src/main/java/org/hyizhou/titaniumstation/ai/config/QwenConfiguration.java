package org.hyizhou.titaniumstation.ai.config;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.qwen.OpenAiApiFlux;
import org.hyizhou.titaniumstation.ai.qwen.QwenAiApi;
import org.hyizhou.titaniumstation.ai.qwen.QwenChatClient;
import org.hyizhou.titaniumstation.ai.qwen.QwenChatOptions;
import org.springframework.ai.autoconfigure.openai.OpenAiChatProperties;
import org.springframework.ai.autoconfigure.openai.OpenAiConnectionProperties;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * qwen自动配置
 * @author hyizhou
 * @date 2024/4/30
 */
@Configuration
@Log4j2
public class QwenConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public QwenChatClient qwenChatClient(
            List<FunctionCallback> toolFunctionCallbacks,
            FunctionCallbackContext functionCallbackContext,
            RetryTemplate retryTemplate
    ){
        // 从环境变量中获取apiKey，后续要部署到服务器，可以增加从配置文件获取
        QwenAiApi api = new QwenAiApi(getApiKey());
        QwenChatOptions options = QwenChatOptions.builder()
                .withMode(QwenAiApi.ChatMode.QWEN_TURBO.value)  // 为开源版本
                .withResultFormat(QwenAiApi.ResultFormat.MESSAGE)
                .build();
        if (!CollectionUtils.isEmpty(toolFunctionCallbacks)){
            options.getFunctionCallbacks().addAll(toolFunctionCallbacks);
        }
        return new QwenChatClient(api, options, retryTemplate, functionCallbackContext);
    }

    /**
     * 因为官方 openai 代码用于 openRouter 时，完成标识符没在库中，会导致反序列化json报错，因此在此修复一下。
     * TODO 未解决
     */
    @Bean
    public OpenAiChatClient openRouterClient(
            OpenAiConnectionProperties commonProperties,
            OpenAiChatProperties chatProperties, RestClient.Builder restClientBuilder,
            List<FunctionCallback> toolFunctionCallbacks, FunctionCallbackContext functionCallbackContext,
            RetryTemplate retryTemplate, ResponseErrorHandler responseErrorHandler
    ){

        var openAiApi = openAiApi(chatProperties.getBaseUrl(), commonProperties.getBaseUrl(),
                chatProperties.getApiKey(), commonProperties.getApiKey(), restClientBuilder, responseErrorHandler);

        if (!CollectionUtils.isEmpty(toolFunctionCallbacks)) {
            chatProperties.getOptions().getFunctionCallbacks().addAll(toolFunctionCallbacks);
        }
        log.info("创建 OpenAiChatClient 成功");
        return new OpenAiChatClient(openAiApi, chatProperties.getOptions(), functionCallbackContext, retryTemplate);
    }

    private OpenAiApi openAiApi(String baseUrl, String commonBaseUrl, String apiKey, String commonApiKey,
                                RestClient.Builder restClientBuilder, ResponseErrorHandler responseErrorHandler) {

        String resolvedBaseUrl = StringUtils.hasText(baseUrl) ? baseUrl : commonBaseUrl;
        Assert.hasText(resolvedBaseUrl, "OpenAI base URL must be set");

        String resolvedApiKey = StringUtils.hasText(apiKey) ? apiKey : commonApiKey;
        Assert.hasText(resolvedApiKey, "OpenAI API key must be set");

        return new OpenAiApiFlux(resolvedBaseUrl, resolvedApiKey, restClientBuilder, responseErrorHandler);
    }

    @Bean
    public FunctionCallbackContext functionCallbackContext(ApplicationContext context) {
        FunctionCallbackContext functionCallbackContext = new FunctionCallbackContext();
        functionCallbackContext.setApplicationContext(context);
        return functionCallbackContext;
    }

    /**
     * 获取通义千问的apiKey
     * @return apiKey
     */
    private String getApiKey() {
        // TODO 从两个地方获取key，一个是环境变量，另一个是配置文件

        return System.getenv("DASHSCOPE_API_KEY");
    }

}

