package org.hyizhou.titaniumstation.ai.config;

import org.hyizhou.titaniumstation.ai.qwen.QwenAiApi;
import org.hyizhou.titaniumstation.ai.qwen.QwenChatClient;
import org.hyizhou.titaniumstation.ai.qwen.QwenChatOptions;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * qwen自动配置
 * @author hyizhou
 * @date 2024/4/30
 */
@Configuration
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

