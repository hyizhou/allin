package org.hyizhou.titaniumstation.ai.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @date 2024/5/20
 */
@Data
@ConfigurationProperties(prefix = QwenProperties.CONFIG_PREFIX)
public class QwenProperties {
    public static final String CONFIG_PREFIX = "spring.ai.qwen";
    private String apikey;
    private String baseUrl;
}
