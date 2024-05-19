package org.hyizhou.titaniumstation.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hyizhou.titaniumstation.common.entity.UserEntity;

/**
 * @date 2024/5/18
 */
@Data
@Entity
@Table(name="user_ai_config")
public class UserAiConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id", nullable = true)
    private HistoryStrategyEntity historyStrategy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id", nullable = true)
    private DialogEntity dialog;
}
