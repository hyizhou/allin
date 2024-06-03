package org.hyizhou.titaniumstation.ai.llmTools.azure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Azure bing搜索配置类
 * @date 2024/6/2
 */
@Data
@ConfigurationProperties(prefix = AzureBingSearchProperties.CONFIG_PREFIX)
public class AzureBingSearchProperties {
    public static final String CONFIG_PREFIX = "spring.ai.tools.azure.bing";
    // 开启 Azure 工具类功能
    private Boolean enabled;
    // key
    private String key;
}
