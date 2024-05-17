package org.hyizhou.titaniumstation.common.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 消息对象
 * @date 2024/5/16
 */
public record ContentModel(
        /*
        对话ID
         */
        @JsonProperty("dialog") String dialog,

        /*
        消息内容，可能为文本、图片、音频、视频等，非文本类型需要使用Base64编码
         */
        @JsonProperty("content") String content,

        /*
        内容类型
         */
        @JsonProperty("type") String type
) {
}
