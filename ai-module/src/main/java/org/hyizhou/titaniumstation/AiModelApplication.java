package org.hyizhou.titaniumstation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"org.hyizhou.titaniumstation.ai.llmTools"})
public class AiModelApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiModelApplication.class, args);
    }
}