package org.hyizhou.titaniumstation.ai.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyizhou.titaniumstation.ai.entity.HistoryStrategyEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;

import java.util.List;

/**
 * 历史上下文，处理历史消息时包装成此对象方便处理
 * @date 2024/5/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryContext {
    /*
    历史消息原始列表，要注意：1. 不应存在system角色消息，2. 是按照时间顺序排列的
     */
    private List<MessageEntity> originalMessages;
    /*
    经过处理后的历史消息列表
     */
    private List<MessageEntity> modifiedMessages;
    /*
    会话总结文本
     */
    private MessageEntity summaryEntity;
    /*
    处理的策略，暂时无用
     */
    private HistoryStrategyEntity strategy;
}
