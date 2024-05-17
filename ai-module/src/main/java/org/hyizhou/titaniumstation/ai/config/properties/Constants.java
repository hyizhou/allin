package org.hyizhou.titaniumstation.ai.config.properties;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

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
}
