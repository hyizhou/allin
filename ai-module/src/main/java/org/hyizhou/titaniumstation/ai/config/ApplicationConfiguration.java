package org.hyizhou.titaniumstation.ai.config;

import org.hyizhou.titaniumstation.ai.processor.ChatRespProcessorChain;
import org.hyizhou.titaniumstation.ai.processor.PromptProcessorChain;
import org.hyizhou.titaniumstation.ai.processor.imp.AppendHistoryPromptProcessor;
import org.hyizhou.titaniumstation.ai.processor.HistoryRepositoryTypeMessage;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 模块的配置类
 * @author hyizhou
 * @date 2024/5/15
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AppendHistoryPromptProcessor appendHistoryPromptProcessor(HistoryRepositoryTypeMessage historyRepository) {
        return new AppendHistoryPromptProcessor(historyRepository);
    }

    /**
     * 创建prompt处理链
     * @param appendHistoryPromptProcessor 添加历史记录的prompt处理器
     * @return 处理链对象
     */
    @Bean
    public PromptProcessorChain promptProcessorChain(
            AppendHistoryPromptProcessor appendHistoryPromptProcessor
    ){
        PromptProcessorChain chain = new PromptProcessorChain();
        chain.addProcessor(appendHistoryPromptProcessor);
        return chain;
    }

    /**
     * 创建chat响应处理链
     * @return 处理链对象
     */
    public ChatRespProcessorChain chatRespProcessorChain() {
        return new ChatRespProcessorChain();
    }
}
