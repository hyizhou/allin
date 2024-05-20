package org.hyizhou.titaniumstation.ai.processor.historyStrategy;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.pojo.HistoryContext;
import org.hyizhou.titaniumstation.ai.service.SimpleChatService;
import org.hyizhou.titaniumstation.ai.tools.TokenTools;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 组合策略类，依次调用其他三种策略，并提供对system消息保护
 * @date 2024/5/18
 */
@Component
@Log4j2
public class CompositeHistoryStrategy implements HistoryStrategyBehavior{
    private final List<HistoryStrategyBehavior> strategies;

    public CompositeHistoryStrategy(SimpleChatService simpleChatService) {
        this.strategies = List.of(
                new MessageSizeStrategy(),
                new TokenSizeStrategy(),
                new SummaryStrategy(simpleChatService)
        );
    }


    @Override
    public HistoryContext applyStrategy(HistoryContext context) {
        log.debug("调用 CompositeHistoryStrategy");
        if (context.getStrategy().isClose()) {
            return context;
        }
        List<MessageEntity> systemMessages = getSystemRoleMessages(context);
        for (HistoryStrategyBehavior strategy : strategies) {
            // 后续策略可能需要基于前一个策略处理的结果进行，因此传递的是已处理的消息列表
            strategy.applyStrategy(context);
        }
        context.getModifiedMessages().addAll(0, systemMessages);
        return context;
    }


    /**
     * 获取消息列表中系统角色消息，并计算token大小、消息条数
     * @param context 上下文
     * @return 系统角色消息列表
     */
    private List<MessageEntity> getSystemRoleMessages(HistoryContext context) {
        List<MessageEntity> systemList = context.getModifiedMessages().stream()
                .filter(messageEntity -> messageEntity.getRole().equals("system"))
                .toList();
        context.getModifiedMessages().removeIf(messageEntity -> messageEntity.getRole().equals("system"));
        if (systemList.isEmpty()){
            return systemList;
        }
        context.setSystemMessageSize(systemList.size());
        int tokenSize = 0;
        for (MessageEntity systemEntity : systemList) {
            tokenSize += TokenTools.simpleCount(systemEntity.getContent());
        }
        context.setSystemTokenSize(tokenSize);
        return systemList;
    }
}
