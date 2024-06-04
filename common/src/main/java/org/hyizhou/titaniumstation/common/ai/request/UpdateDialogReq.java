package org.hyizhou.titaniumstation.common.ai.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 更新对话信息的实体类
 * @date 2024/5/21
 */
public record UpdateDialogReq(
        @JsonProperty("model") String model,
        @JsonProperty("service_provider") String serviceProvider,
        /*
        会话历史策略，为null表示此字段不更新而不是删除，删除请用后面专用字段
         */
        @JsonProperty("history_strategy") HistoryStrategy historyStrategy,
        /*
        删除会话对应的历史策略，这将使之变成用户默认或系统默认
         */
        @JsonProperty("remove_history_strategy") Boolean removeHistoryStrategy,
        @JsonProperty("functions") List<String> functions
) {
    public record HistoryStrategy(
            /*
            关闭策略
             */
            @JsonProperty("close") Boolean isClose,
            @JsonProperty("token_limit") Long tokenLimit,
            @JsonProperty("message_limit") Integer messageLimit,
            @JsonProperty("enable_summary") Boolean enableSummary
    ){}
}
