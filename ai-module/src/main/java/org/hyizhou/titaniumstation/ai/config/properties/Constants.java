package org.hyizhou.titaniumstation.ai.config.properties;

import org.hyizhou.titaniumstation.ai.entity.HistoryStrategyEntity;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;

import java.util.Map;
import java.util.Set;

/**
 * 存储项目中一些常量
 * @date 2024/5/17
 */
public class Constants {
    public static final Set<String> ROLES = Set.of("user", "assistant", "system");
    public static final Map<String, Class<?>> ROLES_MAP = Map.of(
            "user", UserMessage.class,
            "assistant", AssistantMessage.class,
            "system", SystemMessage.class
    );

    /*
    默认对话总结LLM客户端配置
     */
    public static final OpenAiChatOptions SUMMARY_OPTIONS;

    static {
        // 注意这里配置的是 openRouter 站点的模型名，并非 openai 官方，且需要配置 baseUrl 为 openRouter 地址
        SUMMARY_OPTIONS = OpenAiChatOptions.builder()
                .withModel("openai/gpt-3.5-turbo")
                .withMaxTokens(2048)
                .build();
    }

    /*
    全局的默认历史传递策略
     */
    public static final HistoryStrategyEntity GLOBAL_HISTORY_STRATEGY;

    static {
        GLOBAL_HISTORY_STRATEGY = new HistoryStrategyEntity(
                null,
                false,
                500L,
                null,
                HistoryStrategyEntity.SummaryRule.DIALOG_SUMMARY
        );
    }

    /*
    总结历史对话提示词模板
     */
    public static final PromptTemplate SUMMARY_PROMPT_TEMPLATE = new PromptTemplate("""
            You are a helpful assistant.
            You are given a list of messages from a chat conversation.
            Your task is to summarize the conversation in a concise manner.
            You should only respond with the summary.
            
            Here are the messages:
            {text}
            
            Summary:
            """);
}
