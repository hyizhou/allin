package org.hyizhou.titaniumstation.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @date 2024/6/19
 */
@Entity
@Table(name = "prompt_templates")
@Data
@NoArgsConstructor
public class PromptTemplatesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String labels;

    @Column(columnDefinition = "TEXT")
    private String templateText;

    @Column(columnDefinition = "TEXT")
    private String example;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
