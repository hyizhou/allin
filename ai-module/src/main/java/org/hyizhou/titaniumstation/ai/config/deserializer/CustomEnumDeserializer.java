package org.hyizhou.titaniumstation.ai.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.openai.api.OpenAiApi;
import java.io.IOException;

/**
 * 使用OpenAI客户端请求 open router 平台时，会存在例外的 FinishReason 结束符号，这造成反序列化错误，在此进行修复。
 */
@Log4j2
public class CustomEnumDeserializer extends JsonDeserializer<OpenAiApi.ChatCompletionFinishReason> {

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

