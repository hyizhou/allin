package org.hyizhou.titaniumstation.ai.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * open router 平台配置，使用的是openAi客户端，但由于存在一些兼容性问题，在此进行了修复
 *
 * @date 2024/5/20
 */
@Configuration
@Log4j2
public class OpenRouterConfiguration {

    @PostConstruct
    public void openAiApiWebClient() throws NoSuchFieldException, IllegalAccessException {
        // 获取名为"OBJECT_MAPPER"的静态字段
        Field objectMapperField = ModelOptionsUtils.class.getDeclaredField("OBJECT_MAPPER");
        // 设置访问权限（因为该字段可能是private的）
        objectMapperField.setAccessible(true);
        // 通过字段获取静态对象实例
        ObjectMapper objectMapper = (ObjectMapper) objectMapperField.get(null);
        SimpleModule customModule = new SimpleModule();
        customModule.addDeserializer(OpenAiApi.ChatCompletionFinishReason.class, new FinishReasonDeserializationFixer());
        objectMapper.registerModule(customModule);
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.deserializerByType(OpenAiApi.ChatCompletionFinishReason.class, new FinishReasonDeserializationFixer());
        return builder;
    }

    static class FinishReasonDeserializationFixer extends JsonDeserializer<OpenAiApi.ChatCompletionFinishReason> {

        @Override
        public OpenAiApi.ChatCompletionFinishReason deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();
            try {
                return OpenAiApi.ChatCompletionFinishReason.valueOf(value.toUpperCase()); // 尝试按原样转换
            } catch (IllegalArgumentException e) { // 如果转换失败，映射到默认值
                log.info("反序列化 FinishReason 例外值修复，将[{}]映射到 [stop]", value);
                return OpenAiApi.ChatCompletionFinishReason.STOP; // 或者您定义的其他逻辑
            }
        }
    }

}
