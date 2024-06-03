package org.hyizhou.titaniumstation.ai.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 通义千问给与配置的属性
 * @date 2024/5/16
 */
@Setter
@Getter
@ConfigurationProperties(prefix = QwenChatProperties.CONFIG_PREFIX )
public class QwenChatProperties {
    public static final String CONFIG_PREFIX = "spring.ai.qwen.chat";
    /*
     是否启用
     */
    private Boolean enabled;
}
