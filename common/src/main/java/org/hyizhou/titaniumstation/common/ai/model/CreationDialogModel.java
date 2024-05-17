package org.hyizhou.titaniumstation.common.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 创建对话模型
 */
public record CreationDialogModel(
        /*
        服务商名称
         */
        @JsonProperty("serviceProvider") String serviceProvider,
        /*
        模型名称
         */
        @JsonProperty("model") String model
) {
}
