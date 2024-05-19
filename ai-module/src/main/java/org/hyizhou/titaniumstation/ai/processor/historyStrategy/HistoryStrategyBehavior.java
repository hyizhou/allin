package org.hyizhou.titaniumstation.ai.processor.historyStrategy;

import org.hyizhou.titaniumstation.ai.pojo.HistoryContext;

/**
 * 聊天历史记录传输策略的行为接口
 * @date 2024/5/18
 */
public interface HistoryStrategyBehavior {

    /**
     * 应用传输策略
     * @param context 正式调用前需将 originalMessages 属性赋予 历史消息，它通常从数据库提取
     * @return 处理后的对象
     */
    HistoryContext applyStrategy(HistoryContext context);

}
