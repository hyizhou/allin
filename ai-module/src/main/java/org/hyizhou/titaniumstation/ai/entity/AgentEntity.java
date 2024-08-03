package org.hyizhou.titaniumstation.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hyizhou.titaniumstation.common.entity.UserEntity;

import java.time.LocalDateTime;

/**
 * @date 2024/6/19
 */
@Entity
@Table(name = "agents")
@Data
@NoArgsConstructor
public class AgentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String avatar;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_template_id")
    private PromptTemplatesEntity promptTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_strategy_id")
    private HistoryStrategyEntity historyStrategy;

    @Column(columnDefinition = "TEXT")
    private String playbook;

    private String labels;

    private String model;

    private String serviceProvider;

    private Double temperature;

    private Double topP;

    @Column(columnDefinition = "LONGTEXT")
    private String functions;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "is_public")
    private boolean isPublic;
}
