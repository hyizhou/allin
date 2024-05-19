package org.hyizhou.titaniumstation.common.ai.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 对历史传递策略的配置，当某项为空时表示此项关闭
 */
public record HistoryStrategyReq(
        @JsonProperty("dialog_id") String dialogId,  // 对话id
        @JsonProperty("token_size") Long tokenSize,  // token数量限制
        @JsonProperty("message_size") Long messageSize,  //消息条数限制
        @JsonProperty("summary_rule") String summaryRule  // 对话总结生成策略
) { }
