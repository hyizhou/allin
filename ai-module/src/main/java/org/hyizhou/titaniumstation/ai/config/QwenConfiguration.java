package org.hyizhou.titaniumstation.ai.config;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.config.properties.QwenChatProperties;
import org.hyizhou.titaniumstation.ai.config.properties.QwenProperties;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenAiApi;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatClient;
import org.hyizhou.titaniumstation.ai.llmClient.qwen.QwenChatOptions;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@Log4j2
@EnableConfigurationProperties({QwenChatProperties.class, QwenProperties.class})
public class QwenConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = QwenChatProperties.CONFIG_PREFIX, name = "enabled", havingValue = "true",
            matchIfMissing = true)
    public QwenChatClient qwenChatClient(
            List<FunctionCallback> toolFunctionCallbacks,
            FunctionCallbackContext functionCallbackContext,
            RetryTemplate retryTemplate,
            QwenProperties qwenProperties
    ){
        // 从环境变量中获取apiKey，后续要部署到服务器，可以增加从配置文件获取
        QwenAiApi api = new QwenAiApi(getApiKey(qwenProperties));
        QwenChatOptions options = QwenChatOptions.builder()
                .withMode(QwenAiApi.ChatMode.QWEN_TURBO.value)  // 为开源版本
                .withResultFormat(QwenAiApi.ResultFormat.MESSAGE)
                .withIncrementalOutput(true)
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
    private String getApiKey(QwenProperties qwenProperties) {
        if (qwenProperties.getApikey() != null){
            return qwenProperties.getApikey();
        }
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        if (apiKey == null){
            throw new RuntimeException("Qwen（通义千问）必须在配置文件或环境变量（DASHSCOPE_API_KEY）设置key");
        }
        return apiKey;
    }

}

