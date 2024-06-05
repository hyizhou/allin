package org.hyizhou.titaniumstation.ai.llmTools;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * LinkSummary 配置
 * @date 2024/6/5
 */
@Data
@ConfigurationProperties(prefix = LinkSummaryProperties.CONFIG_PREFIX)
public class LinkSummaryProperties {
    public static final String CONFIG_PREFIX = "spring.ai.tools.link-summary";

    private Boolean enabled = false;
}
