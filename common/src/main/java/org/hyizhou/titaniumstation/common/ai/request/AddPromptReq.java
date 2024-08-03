package org.hyizhou.titaniumstation.common.ai.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @date 2024/6/11
 */
public record AddPromptReq(
        @JsonProperty("prompt") String prompt,
        @JsonProperty("description") String description,
        // 是否公开共享
        @JsonProperty("isPublic") Boolean isPublic,
        // 标签
        @JsonProperty("tags") String[] tags
) {
}
