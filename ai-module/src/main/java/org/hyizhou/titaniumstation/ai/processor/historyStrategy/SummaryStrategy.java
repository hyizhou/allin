package org.hyizhou.titaniumstation.ai.processor.historyStrategy;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.config.properties.Constants;
import org.hyizhou.titaniumstation.ai.entity.HistoryStrategyEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.exception.AiSystemException;
import org.hyizhou.titaniumstation.ai.pojo.HistoryContext;
import org.hyizhou.titaniumstation.ai.service.SimpleChatService;
import org.hyizhou.titaniumstation.ai.tools.TokenTools;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 总结对话的策略
 * @date 2024/5/18
 */
@Log4j2
public class SummaryStrategy implements HistoryStrategyBehavior{
    private final SimpleChatService simpleChatService;

    public SummaryStrategy(SimpleChatService simpleChatService) {
        this.simpleChatService = simpleChatService;
    }

    @Override
    public HistoryContext applyStrategy(HistoryContext context) {
        log.debug("调用 SummaryStrategy");
        HistoryStrategyEntity.SummaryRule summaryRule = context.getStrategy().getSummaryRule();
        if (summaryRule == null){
            log.debug("无需调用 SummaryStrategy");
            return context;
        }


        if (context.getModifiedMessages() == null){
            throw new AiSystemException("modifiedMessages不存在，可能是策略处理顺序不对");
        }
        List<MessageEntity> originalMessages = context.getOriginalMessages();
        List<MessageEntity> modifiedMessages = context.getModifiedMessages();
        List<MessageEntity> pendingMessages = originalMessages.stream()
                .filter(messageEntity -> !modifiedMessages.contains(messageEntity))
                .toList();
        if (!needSummary(pendingMessages, modifiedMessages)){
            return context;
        }
        // 计算总结设定的时间
        Duration duration = Duration.ofMillis(100);
        LocalDateTime summaryTime = pendingMessages.get(pendingMessages.size()-1).getTimestamp().plus(duration);

        String pendingText = convertToString(pendingMessages);
        String summaryText = summary(pendingText);
        MessageEntity summaryMessage = new MessageEntity(
                null,
                null,
                null,
                summaryText,
                summaryTime,
                MessageType.USER.getValue(),
                "text"
        );
        modifiedMessages.add(0, summaryMessage);
        context.setSummaryEntity(summaryMessage);
        return context;
    }

    private String convertToString(List<MessageEntity> messages) {
        StringBuilder sb = new StringBuilder();
        for (MessageEntity messageEntity : messages) {
            // 这里是否可以判断一下消息类型，若是图片等消息就不用放入了
            sb.append(messageEntity.getRole()).append(":");
            sb.append(messageEntity.getContent()).append("\n\n");
        }
        return sb.toString();
    }

    private String summary(String text) {
        int tokenSize = TokenTools.simpleCount(text);
        final int MAX_TOKEN_SIZE = 3000;
        if (tokenSize > MAX_TOKEN_SIZE){
            // token 长度限制，避免太长历史撑爆钱包
            text = TokenTools.subString(text, tokenSize - MAX_TOKEN_SIZE, tokenSize);
        }
        Map<String, Object> map = Map.of("text", text);
        PromptTemplate promptTemplate = Constants.SUMMARY_PROMPT_TEMPLATE;
        UserMessage message = (UserMessage) promptTemplate.createMessage(map);
        if (log.isDebugEnabled()) {
            log.debug("待总结的对话\n{}", message.getContent());
        }
        ChatResponse chatResponse = simpleChatService.chat(
                new Prompt(message, Constants.SUMMARY_OPTIONS),
                "openai"
        );
        if (log.isDebugEnabled()){
            log.debug("历史总结的结果：\n{}", chatResponse.getResult().getOutput().getContent());
        }
        return chatResponse.getResult().getOutput().getContent();
    }

    /**
     * 判断是否需要进行内容总结
     * @param messages 待总结的消息列表
     * @return true就表需要总结
     */
    private boolean needSummary(List<MessageEntity> messages, List<MessageEntity> modifiedMessages) {
        if (messages.isEmpty()){
            return false;
        }
        // 若只有一个元素且这是上一次的总结内容，就无需进行总结，使用上次总结内容即可
        if (messages.size() == 1 && messages.get(0).getIsSummary()){
            modifiedMessages.add(0, messages.get(0));
            return false;
        }
        return true;
    }
}
