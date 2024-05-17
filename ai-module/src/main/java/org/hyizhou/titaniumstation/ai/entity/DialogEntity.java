package org.hyizhou.titaniumstation.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hyizhou.titaniumstation.common.entity.UserEntity;

import java.time.LocalDateTime;

/**
 * 对话表实体类
 * @date 2024/5/16
 */
@Data
@Entity
@Table(name = "dialogs")
public class DialogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String dialogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 假设有一个User实体类代表用户

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String serviceProvider;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(columnDefinition = "TEXT")
    private String dialogsSummary; // 假设使用TEXT类型存储对话总结

    // 构造函数
    public DialogEntity() {}
}
