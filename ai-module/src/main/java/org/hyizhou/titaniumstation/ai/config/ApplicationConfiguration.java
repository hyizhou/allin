package org.hyizhou.titaniumstation.ai.config;

import org.hyizhou.titaniumstation.ai.config.deserializer.CustomEnumDeserializer;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * 模块的配置类
 * @author hyizhou
 * @date 2024/5/15
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.deserializerByType(OpenAiApi.ChatCompletionFinishReason.class, new CustomEnumDeserializer());
        return builder;
    }

}
