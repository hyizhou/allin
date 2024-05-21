package org.hyizhou.titaniumstation.common.ai.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * 会话信息
 */
public record DialogInfoResp(
        @JsonProperty("dialog_id") String dialogId,
        @JsonProperty("create_time") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createTime,
        @JsonProperty("model") String model,
        @JsonProperty("service_provider") String serviceProvider,
        /*
        会话总结
         */
        @JsonProperty("dialogs_summary") String dialogsSummary,
        /*
        对历史策略，若为null则表示使用用户或系统默认
         */
        @JsonProperty("history_strategy") HistoryStrategy historyStrategy
) {
    public record HistoryStrategy(
            /*
            关闭策略
             */
            @JsonProperty("close") Boolean isClose,
            @JsonProperty("token_limit") Long tokenLimit,
            @JsonProperty("message_limit") Integer messageLimit,
            @JsonProperty("enable_summary") Boolean enableSummary
    ){ }
}
