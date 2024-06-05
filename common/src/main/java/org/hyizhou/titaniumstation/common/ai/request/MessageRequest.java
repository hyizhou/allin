package org.hyizhou.titaniumstation.common.ai.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 对大语言模型的一次消息请求
 * @author hyizhou
 * @date 2024/5/15
 */
public record MessageRequest(
        @JsonProperty("model") String modelName,
        @JsonProperty("role") String role,
        @JsonProperty("message") String message,
        @JsonProperty("client") String client,  // LLM客户端
        @JsonProperty("functions")List<String> functions  // 启用的函数工具名
        ) {}
