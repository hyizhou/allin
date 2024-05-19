package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.pojo.HistoryContext;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.service.HistoryStrategyService;
import org.springframework.stereotype.Component;

/**
 * 获取历史记录追加到消息中
 * @date 2024/5/17
 */
@Component
public class HistoryAppendProcessor implements MessageProcessor {
    private final HistoryStrategyService historyStrategyService;

    public HistoryAppendProcessor(HistoryStrategyService historyStrategyService) {
        this.historyStrategyService = historyStrategyService;
    }

    @Override
    public MessageContext process(MessageContext context) {
        HistoryContext historyContext = historyStrategyService.applyStrategy(context.getDialog());
        context.setHistoryMessages(historyContext.getModifiedMessages());
        context.setSummaryEntity(historyContext.getSummaryEntity());
        return context;
    }

//    private HistoryContext generateHistoryContext(List<MessageEntity> messages, HistoryStrategyEntity strategy) {
//        return new HistoryContext(
//                messages,
//                messages,
//                null,
//                strategy
//        );
//    }
}
