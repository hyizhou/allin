package org.hyizhou.titaniumstation.common.ai.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 创建对话模型
 */
public record CreationDialogReq(
        /*
        服务商名称，等同于 MessageRequest.client
         */
        @JsonProperty("serviceProvider") String serviceProvider,
        /*
        模型名称
         */
        @JsonProperty("model") String model
) {
}
