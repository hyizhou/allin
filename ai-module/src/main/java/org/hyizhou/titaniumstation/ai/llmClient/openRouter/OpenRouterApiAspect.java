package org.hyizhou.titaniumstation.ai.llmClient.openRouter;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 针对 api 相关方法添加日志
 *
 * @date 2024/6/3
 */
@Aspect
@Component
@Log4j2
public class OpenRouterApiAspect {
    @Before("execution(* org.springframework.ai.openai.OpenAiChatClient.call(..))")
    public void serviceLayerExecution(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Prompt prompt) {
            if (!log.isDebugEnabled()){
                return;
            }
            List<Message> messageList = prompt.getInstructions();
            StringBuffer messageStr = new StringBuffer();
            for (Message message : messageList) {
                messageStr.append(message.getMessageType().getValue()).append(" -> ").append(message.getContent()).append("\n");
            }
            log.debug("大语言模型请求消息：\n{}", messageStr);
        }
    }

//    @Before("execution(* org.springframework.ai.openai.api.OpenAiApi.chatCompletionEntity(..))")
    // 增强 OpenAiApi 不可行，因为并未将其注入容器中
    public void chatCompletionEntityExecution(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof OpenAiApi.ChatCompletionRequest chatRequest) {
            String toJson = ModelOptionsUtils.toJsonString(chatRequest);
            log.debug("大语言模型请求体：\n{}", toJson);
        }
    }
}
