package org.hyizhou.titaniumstation.ai.config;

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
 * @author hyizhou
 * @date 2024/5/15
 */
@Configuration
@EnableConfigurationProperties({AzureBingSearchProperties.class})
public class ApplicationConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 注入大语言模型所用搜索引擎工具到容器
     * @param azureBingSearchProperties 配置
     * @return 将函数调用自动添加到大语言模型请求报文需要使用此类型
     */
    @Bean
    @ConditionalOnProperty(prefix = AzureBingSearchProperties.CONFIG_PREFIX, name = "enabled", havingValue = "true")
    public FunctionCallback bingWebSearch(AzureBingSearchProperties azureBingSearchProperties){
        return FunctionCallbackWrapper.builder(new BingWebSearch(azureBingSearchProperties.getKey()))
                .withDescription("搜索引擎在线搜索")
                .withName("searchWeb")
                .build();
    }



}
