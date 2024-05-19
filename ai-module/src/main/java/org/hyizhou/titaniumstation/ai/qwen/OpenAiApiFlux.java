package org.hyizhou.titaniumstation.ai.qwen;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

/**
 * @date 2024/5/18
 */
public class OpenAiApiFlux extends OpenAiApi {
    public OpenAiApiFlux(String openAiToken) {
        super(openAiToken);
    }

    public OpenAiApiFlux(String baseUrl, String openAiToken) {
        super(baseUrl, openAiToken);
    }

    public OpenAiApiFlux(String baseUrl, String openAiToken, RestClient.Builder restClientBuilder) {
        super(baseUrl, openAiToken, restClientBuilder);
    }

    public OpenAiApiFlux(String baseUrl, String openAiToken, RestClient.Builder restClientBuilder, ResponseErrorHandler responseErrorHandler) {
        super(baseUrl, openAiToken, restClientBuilder, responseErrorHandler);
    }
    /**
     * The reason the model stopped generating tokens.
     */
    public enum ChatCompletionFinishReason {
        /**
         * The model hit a natural stop point or a provided stop sequence.
         */
        @JsonProperty("stop") STOP,
        /**
         * The maximum number of tokens specified in the request was reached.
         */
        @JsonProperty("length") LENGTH,
        /**
         * The content was omitted due to a flag from our content filters.
         */
        @JsonProperty("content_filter") CONTENT_FILTER,
        /**
         * The model called a tool.
         */
        @JsonProperty("tool_calls") TOOL_CALLS,
        /**
         * (deprecated) The model called a function.
         */
        @JsonProperty("function_call") FUNCTION_CALL,
        /**
         * Only for compatibility with Mistral AI API.
         */
        @JsonProperty("tool_call") TOOL_CALL,
        /**
         * 新增的一种情况
         */
        @JsonProperty("eos") EOS
    }
}
