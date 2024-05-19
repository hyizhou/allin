package org.hyizhou.titaniumstation.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 历史传递策略表
 * @date 2024/5/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="history_strategy")
public class HistoryStrategyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strategyId;

    @Column(columnDefinition = "boolean default false")
    private boolean close;

    @Column(nullable = true)
    private Long tokenSize;

    @Column(nullable = true)
    private Integer messageSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SummaryRule summaryRule;


    // 枚举类型定义
    public enum SummaryRule {
        KEYWORD_RETENTION,  // 关键字留存
        DIALOG_SUMMARY  // 对话总结
    }
}
