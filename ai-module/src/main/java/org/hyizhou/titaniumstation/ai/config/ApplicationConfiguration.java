package org.hyizhou.titaniumstation.ai.config;

import org.hyizhou.titaniumstation.ai.llmTools.LinkSummary;
import org.hyizhou.titaniumstation.ai.llmTools.LinkSummaryProperties;
import org.hyizhou.titaniumstation.ai.llmTools.TitaniumPython;
import org.hyizhou.titaniumstation.ai.llmTools.azure.AzureBingSearchProperties;
import org.hyizhou.titaniumstation.ai.llmTools.azure.BingWebSearch;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 模块的总配置类
 *
 * @author hyizhou
 * @date 2024/5/15
 */
@Configuration
@EnableConfigurationProperties({AzureBingSearchProperties.class, LinkSummaryProperties.class})
public class ApplicationConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 搜索引擎工具，供大模型调用
     *
     * @param azureBingSearchProperties 配置
     * @return 将函数调用自动添加到大语言模型请求需要使用此类型
     */
    @Bean
    @ConditionalOnProperty(prefix = AzureBingSearchProperties.CONFIG_PREFIX, name = "enabled", havingValue = "true")
    public FunctionCallback bingWebSearch(AzureBingSearchProperties azureBingSearchProperties, TitaniumPython titaniumPython) {
        return FunctionCallbackWrapper.builder(new BingWebSearch(
                        azureBingSearchProperties.getKey(),
                        azureBingSearchProperties.getOpenLink(),
                        titaniumPython
                ))
                .withDescription("搜索引擎在线搜索")
                .withName("searchWeb")
                .build();
    }

    /**
     * 链接摘要工具，供大模型调用
     * @param titaniumPython 调用 python 的接口
     * @return 将函数调用自动添加到大语言模型请求需要使用此类型
     */
    @Bean
    @ConditionalOnProperty(prefix = LinkSummaryProperties.CONFIG_PREFIX, name = "enabled", havingValue = "true")
    public FunctionCallback linkSummary(TitaniumPython titaniumPython) {
        return FunctionCallbackWrapper.builder(new LinkSummary(titaniumPython))
                .withDescription("传入链接读取页面摘要")
                .withName("linkSummary")
                .build();
    }


}
