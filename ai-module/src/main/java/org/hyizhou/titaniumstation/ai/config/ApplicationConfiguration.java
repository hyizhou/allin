package org.hyizhou.titaniumstation.ai.config;

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



}
