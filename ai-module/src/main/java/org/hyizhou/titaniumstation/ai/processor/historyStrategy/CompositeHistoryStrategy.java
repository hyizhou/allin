package org.hyizhou.titaniumstation.ai.processor.historyStrategy;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.pojo.HistoryContext;
import org.hyizhou.titaniumstation.ai.service.SimpleChatService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 组合策略类，一次性跑完全部
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
        for (HistoryStrategyBehavior strategy : strategies) {
            // 后续策略可能需要基于前一个策略处理的结果进行，因此传递的是已处理的消息列表
            strategy.applyStrategy(context);
        }
        return context;
    }
}
