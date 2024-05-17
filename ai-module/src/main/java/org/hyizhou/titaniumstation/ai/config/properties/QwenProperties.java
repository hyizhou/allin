package org.hyizhou.titaniumstation.ai.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 通义千问给与配置的属性
 * @date 2024/5/16
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.ai.qwen")
public class QwenProperties {
    private String apikey;
    private String baseUrl;
}
